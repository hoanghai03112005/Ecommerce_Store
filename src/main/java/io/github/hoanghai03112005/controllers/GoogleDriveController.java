package io.github.hoanghai03112005.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.hoanghai03112005.models.GoogleToken;
import io.github.hoanghai03112005.services.GoogleDriveService;


@Controller
public class GoogleDriveController {
	@Autowired
	private GoogleDriveService googleDriveService;
	
	@RequestMapping("/oauth2callback")
	public String oauth2callback(@RequestParam("code") String code, Model model) {
		try {
			System.out.println("Received code: " + code);
			GoogleToken token = googleDriveService.exchangeCodeForTokens(code);
            model.addAttribute("token", token);
            return "redirect:/admin";
		} catch (IOException e) {
			 e.printStackTrace();
			 return "redirect:/google-auth";
		}
	}
	
}
