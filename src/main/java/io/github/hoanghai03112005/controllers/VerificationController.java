package io.github.hoanghai03112005.controllers;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.github.hoanghai03112005.models.User;
import io.github.hoanghai03112005.repositories.UserRepository;
import io.github.hoanghai03112005.services.EmailService;
import io.github.hoanghai03112005.services.UserService;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class VerificationController {

    private final AdminViewController adminViewController;
	@Autowired
    private final EmailService emailService;
	@Autowired
    private final UserService userService;
	@Autowired
	private UserRepository userRepository;

    VerificationController(UserService userService, EmailService emailService, AdminViewController adminViewController) {
        this.userService = userService;
        this.emailService = emailService;
        this.adminViewController = adminViewController;
    }
	
//	@GetMapping("/verify")
//	public String verifyAccount(@RequestParam String token, RedirectAttributes redirectAttributes) {
//		Optional<User> optionalUser = userRepository.findByVerificationToken(token);
//		
//		if(optionalUser.isEmpty()) {
//			 redirectAttributes.addFlashAttribute("error", "Token không hợp lệ");
//		     return "redirect:/"; 
//		}
//		
//		if(optionalUser.get().getTokenExpiration().isBefore(LocalDateTime.now())) {
//			User user = optionalUser.get();
//			userRepository.delete(user);
//		    redirectAttributes.addFlashAttribute("error", "Token đã hết hạn, vui lòng đăng ký lại.");
//		    return "redirect:/"; 
//		}
//		
//		User user = optionalUser.get();
//		user.setEnabled(true);
//		user.setVerificationToken(null);
//		userRepository.save(user);
//		
//		redirectAttributes.addFlashAttribute("message", "Xác thực thành công! Bạn có thể đăng nhập ngay.");
//	    return "redirect:/login"; 
//		
//	}
	
	

	
}
