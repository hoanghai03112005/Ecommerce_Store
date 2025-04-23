package io.github.hoanghai03112005.services;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.github.hoanghai03112005.dtos.ProductDTO;
import io.github.hoanghai03112005.models.Category;
import io.github.hoanghai03112005.models.Product;
import io.github.hoanghai03112005.repositories.ProductRepository;
import jakarta.transaction.Transactional;

@Service
public class ProductService {
	
	@Autowired
	private DriveService driveService; 
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryService categoryService;
	
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}
	
	public void saveProduct(ProductDTO dto) throws IOException {
	    String imageUrl = saveImage(dto.getImage());
	    Category category = categoryService.getCategoryByID(dto.getCategory_id());

	    MultipartFile zip = dto.getModelFile();
	    String zipFilePath = saveZipTemporarily(zip); // Lưu tạm để upload
	    File zipFile = new File(zipFilePath);

	    try {
	        // Upload lên Google Drive
	        String zipFileUrl = driveService.uploadFileToDrive(zipFilePath);
	        long fileSize = zipFile.length();

	        double fileSizeInMB = fileSize / 1024.0 / 1024.0;
	        BigDecimal roundedFileSize = new BigDecimal(fileSizeInMB).setScale(2, RoundingMode.HALF_UP);

	        // Tạo Product và lưu vào DB
	        Product product = new Product();
	        product.setName(dto.getName());
	        product.setPrice(dto.getPrice());
	        product.setCategory(category);
	        product.setFormat(dto.getFormat());
	        product.setStatus(dto.isStatus());
	        product.setImage_url(imageUrl);
	        product.setZipFileUrl(zipFileUrl);
	        product.setFileSize(roundedFileSize.doubleValue());
	        product.setIsDownloadable(false);

	        productRepository.save(product);
	    } finally {
	        // Xóa file tạm kể cả khi có lỗi
	        if (zipFile.exists()) {
	            zipFile.delete();
	        }
	    }
	}

	
	public Product getProductById(Long id) {
		return productRepository.findById(id).orElse(null);
	}
	
	private String saveImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            return "uploads/default.jpg"; // fallback
        }

        String uploadDir = "uploads/";
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(image.getInputStream(), filePath);

        return filePath.toString(); // đường dẫn để hiển thị trong web
    }
	
	private String saveZipTemporarily(MultipartFile file) throws IOException {
        String tempDir = "temp/";
        File tempFolder = new File(tempDir);
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(tempDir, fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }
	
	public boolean existByName(String name) {
		return productRepository.existsByName(name);
	}
	
	@Transactional
	public void updateProduct(Long id, ProductDTO dto) throws IOException {
	    Product existingProduct = productRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));

	    // 1. Xử lý ảnh mới nếu có
	    String imageUrl = existingProduct.getImage_url();
	    if (dto.getImage() != null && !dto.getImage().isEmpty()) {
	        imageUrl = saveImage(dto.getImage());
	    }

	    // 2. Xử lý file ZIP mới nếu có
	    String zipFileUrl = existingProduct.getZipFileUrl();
	    Double fileSize = existingProduct.getFileSize();

	    if (dto.getModelFile() != null && !dto.getModelFile().isEmpty()) {
	        MultipartFile zip = dto.getModelFile();

	        String zipFilePath = saveZipTemporarily(zip); 
	        zipFileUrl = driveService.uploadFileToDrive(zipFilePath); 

	        long fileLength = new File(zipFilePath).length();
	        double fileSizeInMB = fileLength / 1024.0 / 1024.0;
	        BigDecimal rounded = new BigDecimal(fileSizeInMB).setScale(2, RoundingMode.HALF_UP);
	        fileSize = rounded.doubleValue();

	        new File(zipFilePath).delete();
	    }

	    Category category = categoryService.getCategoryByID(dto.getCategory_id());

	    existingProduct.setName(dto.getName());
	    existingProduct.setPrice(dto.getPrice());
	    existingProduct.setFormat(dto.getFormat());
	    existingProduct.setStatus(dto.isStatus());
	    existingProduct.setCategory(category);
	    existingProduct.setImage_url(imageUrl);
	    existingProduct.setZipFileUrl(zipFileUrl);
	    existingProduct.setFileSize(fileSize);

	    productRepository.save(existingProduct);
	}

	public void deleteProduct(Long id) {
        productRepository.deleteById(id);
	}
	
	public Product findByID(Long id) {
		return productRepository.findById(id).orElse(null);
	}
	
	public List<Product> getAllActiveProduct() {
		return productRepository.findAllByStatusTrueOrderByIdDesc();
	}
	
	public List<Product> getAllByStatusTrueAndCategory_Name(String categoryName) {
		return productRepository.findAllByStatusTrueAndCategory_Name(categoryName);
	}
	
	public List<Product> searchProduct(String keyword) {
		return productRepository.findAllByStatusTrueAndNameContainingIgnoreCase(keyword);
	}
}
