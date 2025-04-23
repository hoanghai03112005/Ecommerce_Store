package io.github.hoanghai03112005.services;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class CommonServiceImpl {
	private final HttpSession session;
	
	public CommonServiceImpl(HttpSession session) {
        this.session = session;
    }
	
	public void removeSessionMessage() {
		session.removeAttribute("errorMsg");
		session.removeAttribute("successMsg");
	}
}
