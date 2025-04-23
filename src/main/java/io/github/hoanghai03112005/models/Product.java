package io.github.hoanghai03112005.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@Column(nullable = false)
	private Double price;
	
	@Column(nullable = false)
	private String image_url;
	
	@Column(nullable = false, unique = true)
	private String zipFileUrl; // link file
	
	private double fileSize;
	private String format; // Định dạng file (OBJ, STL, GLTF, FBX, ...)
	
	private boolean status;
	
	@Column(nullable = false)
	private Boolean isDownloadable;
	
	@ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
	
//	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Order> orders; 
	public Product() {
		// TODO Auto-generated constructor stub
	}
	
	public Product(String name, Double price, String imageUrl, String zipFileUrl,
            long fileSize, String format, Boolean isDownloadable, Category category) {
		 this.name = name;
		 this.price = price;
		 this.image_url = imageUrl;
		 this.zipFileUrl = zipFileUrl;
		 this.fileSize = fileSize;
		 this.format = format;
		 this.isDownloadable = isDownloadable;
		 this.category = category;
		}


	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getZipFileUrl() {
		return zipFileUrl;
	}

	public void setZipFileUrl(String zipFileUrl) {
		this.zipFileUrl = zipFileUrl;
	}

	public double getFileSize() {
		return fileSize;
	}

	public void setFileSize(double fileSize) {
		this.fileSize = fileSize;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Boolean getIsDownloadable() {
		return isDownloadable;
	}

	public void setIsDownloadable(Boolean isDownloadable) {
		this.isDownloadable = isDownloadable;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
}
