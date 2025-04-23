package io.github.hoanghai03112005.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import io.github.hoanghai03112005.models.User;
import io.github.hoanghai03112005.repositories.UserRepository;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private UserRepository userRepository;
	
	public void sendVerificationEmail(User user) {
		String token = UUID.randomUUID().toString(); // tạo mã
		user.setVerificationToken(token);
		userRepository.save(user);
		
		String verificationLink = "http://localhost:8082/verify?token=" + token;
		String subject = "Email Verification";
		String body = "Click the link to verify your account: " + verificationLink;
		
		sendEmail(user.getEmail(), subject, body);
	}
	
	public void sendResetPasswordEmail(User user) {
		String token = UUID.randomUUID().toString();
		user.setResetToken(token);
		user.setTokenExpiration(LocalDateTime.now().plusMinutes(5));
		userRepository.save(user);

	    String resetLink = "http://localhost:8082/reset-password?token=" + token;
	    String subject = "Reset Your Password";
	    String body = "Click the link below to reset your password: \n" + resetLink;
	    
	    sendEmail(user.getEmail(), subject, body);
	}
	
	private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
	
}
