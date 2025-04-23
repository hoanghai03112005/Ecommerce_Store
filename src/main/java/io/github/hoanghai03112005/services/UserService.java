package io.github.hoanghai03112005.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import io.github.hoanghai03112005.controllers.AdminViewController;
import io.github.hoanghai03112005.dtos.UserRegistrationDTO;
import io.github.hoanghai03112005.enums.Role;
import io.github.hoanghai03112005.models.User;
import io.github.hoanghai03112005.repositories.CategoryRepository;
import io.github.hoanghai03112005.repositories.UserRepository;

@Service
public class UserService {

    private final CategoryRepository categoryRepository;
	
	@Autowired
    private final EmailService emailService;

    private final AdminViewController adminViewController;
	@Autowired
	private  UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

    UserService(AdminViewController adminViewController, EmailService emailService, CategoryRepository categoryRepository) {
        this.adminViewController = adminViewController;
        this.emailService = emailService;
        this.categoryRepository = categoryRepository;
    }
	
	public List<User> getAllUser() {
		return userRepository.findAll();
	}
	
	public User getUserByID(Integer id) {
		return userRepository.findById(id).orElse(null);
	}
	
	public User registerUser(UserRegistrationDTO dto) throws IOException {
		String encodedPassword = passwordEncoder.encode(dto.getPassword());
		String imageUrl = saveImage(dto.getImage());
		
		User user = new User(dto.getName(), dto.getEmail(), encodedPassword, dto.getPhone(), imageUrl);
		user.setRole(Role.USER);
		user.setTokenExpiration(LocalDateTime.now().plusMinutes(5));
		userRepository.save(user);
		
		emailService.sendVerificationEmail(user);
		
		return user;
		
		
	}
	
	private String saveImage(MultipartFile image) throws IOException {
		if (image == null || image.isEmpty()) {
            return "default.jpg"; 
        }
		
		String uploadDir = "uploads/";
		File uploadFolder = new File(uploadDir);
		
		if(!uploadFolder.exists()) {
			uploadFolder.mkdir();
		}
		
		 String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename(); // Đổi tên file tránh trùng
		 Path filePath = Paths.get(uploadDir, fileName);
		 
		 Files.copy(image.getInputStream(), filePath);
		 System.out.println("Uploading file: " + filePath.toString());

		
		return filePath.toString();
 	}
	
	public void updateUser(String email, User updatedUser) {
		User existingUser = getUserByEmail(email);
		existingUser.setName(updatedUser.getName());
		existingUser.setPhone(updatedUser.getPhone());
		userRepository.save(existingUser);
	}
	
	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		
		return userRepository.findByEmail(email).orElse(null);
	}
	
	 public User getUserByEmail(String email) {
	        return userRepository.findByEmail(email).orElse(null);
	    }
	
	public boolean existsByName(String name) {
		return userRepository.existsByName(name);
	}
	
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	
	public boolean existsByPhone(String phone) {
		return userRepository.existsByPhone(phone);
	}
	
	public boolean isPasswordMatch(String password, String confirmPassword) {
	    return password != null && password.equals(confirmPassword);
	}
	
	public User findByResetToken(String token) {
		return userRepository.findByResetToken(token).orElse(null);
	}
}
