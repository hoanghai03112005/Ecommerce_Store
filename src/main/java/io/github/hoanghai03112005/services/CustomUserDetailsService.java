package io.github.hoanghai03112005.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.hoanghai03112005.models.User;
import io.github.hoanghai03112005.repositories.UserRepository;
import io.github.hoanghai03112005.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	 System.out.println("🔍 loadUserByUsername called with email: " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản với email: " + email));
        

        System.out.println("User found: " + user.getEmail() + " | Role: " + user.getRole().name());
        return new CustomUserDetails(user);
    }
}
