package com.rpa.sapsf.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rpa.sapsf.business.UserBusinessLogic;
import com.rpa.sapsf.dto.UserDTO;

@RestController
@RequestMapping("/api")
public class UserController {

	private UserBusinessLogic businessLogic = new UserBusinessLogic();

	@PostMapping("/user-registration")
	public UserDTO userRegistration(@RequestBody UserDTO user) {
		UserDTO response = new UserDTO();
		try {
			response = businessLogic.userRegistration(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@PostMapping("/user-login")
	public UserDTO userLogin(@RequestBody UserDTO user) {
		UserDTO response = new UserDTO();
		try {
			response = businessLogic.userLogin(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@PostMapping("/user-forgot-password")
	public UserDTO userForgotPassword(@RequestBody UserDTO user) {
		UserDTO response = new UserDTO();
		try {
			response = businessLogic.userForgotPassword(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping("/say-hello")
	public String sayHello() {

		return "Hello Guru!!!";
	}
}
