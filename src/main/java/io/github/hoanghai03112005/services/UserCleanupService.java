package io.github.hoanghai03112005.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.github.hoanghai03112005.models.User;
import io.github.hoanghai03112005.repositories.UserRepository;

@Service
public class UserCleanupService {
	@Autowired
	private UserRepository userRepository;
	
	@Scheduled(fixedRate = 60000) // 1p
	public void deleteExpiredUsers() {
		List<User> expiredUsers = userRepository.findByEnabledFalseAndTokenExpirationBefore(LocalDateTime.now());
		userRepository.deleteAll(expiredUsers);
	}
}
