package io.github.hoanghai03112005.dtos;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegistrationDTO {
	
	@NotBlank(message = "Tên không được để trống")
	private String name;
	
	@Email(message = "Email không đúng định dạng")
	@NotBlank(message = "Email không được để trống")
	private String email;
	
	@NotBlank(message = "Mật khẩu không được để trống")
	@Size(min = 6, message = "Mật khẩu phải ít nhất 6 kí tự")
	private String password;
	
	@NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;
	
	@NotBlank(message = "Số điện thoại không được để trống")
	private String phone;
	
	private MultipartFile image;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
	
}
