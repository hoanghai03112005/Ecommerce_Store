package io.github.hoanghai03112005.components;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		String redirectUrl = "/";
		  System.out.println("User authenticated: " + authentication.getName());
		  System.out.println("Authorities: " + authentication.getAuthorities());
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		for (GrantedAuthority grantedAuthority : authorities) {
			if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
				redirectUrl = "/";
				break;
			} else if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
				redirectUrl = "/";
				break;
			}
		}
		response.sendRedirect(redirectUrl);
	}
}
