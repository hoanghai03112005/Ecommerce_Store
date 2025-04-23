package io.github.hoanghai03112005.dtos;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductDTO {
	@NotBlank(message = "Không được để trống tên")
	private String name;
	
	@NotNull(message = "Giá không được để trống")
	@DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
	private Double price;
	
	@NotNull(message = "Vui lòng chọn danh mục")
	private Integer category_id;
	
	private String format;
	
	private boolean status;
	
	private MultipartFile image;
	private MultipartFile modelFile;
	public String getName() {
		return name;
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
	public Integer getCategory_id() {
		return category_id;
	}
	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public MultipartFile getImage() {
		return image;
	}
	public void setImage(MultipartFile image) {
		this.image = image;
	}
	public MultipartFile getModelFile() {
		return modelFile;
	}
	public void setModelFile(MultipartFile modelFile) {
		this.modelFile = modelFile;
	}
	
	
}
