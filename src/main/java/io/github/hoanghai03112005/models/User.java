package io.github.hoanghai03112005.models;

import java.time.LocalDateTime;

import io.github.hoanghai03112005.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false, unique = true)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role = Role.USER;
	
	@Column(nullable = false)
	private String phone;
	
	@Column(name = "image_url")
	private String image_url;
	
	@Column(name = "enabled")
	private boolean enabled = false;
	
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	
	@Column(name = "verification_token")
	private String verificationToken;
	
	@Column(name = "token_expiration")
	 private LocalDateTime tokenExpiration;
	
	@Column(name = "reset_token")
	private String resetToken;
	
	public User() {
		// TODO Auto-generated constructor stub
	}

	

	public User(String name, String email, String password, String phone, String image_url) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.image_url = image_url;
	}

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public LocalDateTime getTokenExpiration() {
		return tokenExpiration;
	}

	public void setTokenExpiration(LocalDateTime tokenExpiration) {
		this.tokenExpiration = tokenExpiration;
	}

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	
	
}
