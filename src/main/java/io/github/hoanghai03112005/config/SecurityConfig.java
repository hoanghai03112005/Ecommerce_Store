package io.github.hoanghai03112005.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import io.github.hoanghai03112005.components.CustomAuthenticationSuccessHandler;
import io.github.hoanghai03112005.services.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	private final CustomUserDetailsService customUserDetailService;
	
	public SecurityConfig(CustomUserDetailsService customUserDetailService, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
		// TODO Auto-generated constructor stub
		this.customUserDetailService = customUserDetailService;
		// TODO Auto-generated constructor stub
		this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")// Chỉ Admin được vào /admin
                .anyRequest().permitAll()
            )
        .formLogin(login -> login  // Bật form login
                .loginPage("/login")
                .usernameParameter("email") 
                .passwordParameter("password")
                .successHandler(customAuthenticationSuccessHandler())
                .permitAll()
            )
        .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .permitAll()
            )
        .rememberMe(remember -> remember
        		.key("uniqueAndSecret")
        		.tokenValiditySeconds(60 * 24 * 60 * 60)
        		.rememberMeParameter("remember-me")
        	)
        .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) 
                .maximumSessions(1)
                .expiredUrl("/login?expired")
            );
        ;
        return http.build();
    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder);
		
		return new ProviderManager(List.of(authProvider));
		
//		return authenticationConfiguration.getAuthenticationManager();
	}
	

	public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
	    return new CustomAuthenticationSuccessHandler();
	}
}
