package com.medai.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.medai.entities.User;
import com.medai.repo.UserRepo;

import org.springframework.ui.Model;
@Controller
public class UserController {
	private UserRepo repo;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	public UserController(UserRepo repo) {
		this.repo=repo;
	}
	
	@GetMapping("/index")
	public String getHomePage() {
		return "home";
	}
	
	@GetMapping("/signin")
	public String getLoginPage() {
		return "loginpage";
	}
	
	@GetMapping("/register")
	public String getSingupPage(Model model) {
		model.addAttribute("user",new User());
		return "signuppage";
	}
	
	@PostMapping("/register-process")
	public String doRegister(@ModelAttribute("user") User user, @RequestParam("profileImg") MultipartFile file) {
		try {
			if(file.isEmpty()) {
				user.setImageUrl("default.jpg");
			}else {
				user.setImageUrl(file.getOriginalFilename());
				File savefile = new ClassPathResource("/static/img").getFile();
				Path path = Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			user.setRole("ROLE_USER");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			repo.save(user);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return "signuppage";
	}
	
	
}
