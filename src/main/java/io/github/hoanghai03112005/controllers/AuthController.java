package io.github.hoanghai03112005.controllers;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController {
	@GetMapping("/google-auth")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        String redirectUri = "http://localhost:8082/oauth2callback";
        String clientId = "31624690914-svighf5g450ldhq6v414ati1g19cb80m.apps.googleusercontent.com";
        String scope = "https://www.googleapis.com/auth/drive.file";

        String authUrl = "https://accounts.google.com/o/oauth2/auth"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&scope=" + scope
                + "&access_type=offline"
                + "&prompt=consent";

        response.sendRedirect(authUrl);
    }
}
