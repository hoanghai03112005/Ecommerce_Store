package io.github.hoanghai03112005.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/user")
public class UserController {
	@GetMapping({"", "/"})
	public String home() {
		return "admin/users/user-home";
	}
	
}
