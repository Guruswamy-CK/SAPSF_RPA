package com.rpa.sapsf.business;

import com.rpa.sapsf.dto.UserDTO;
import com.rpa.sapsf.service.UserService;
import com.rpa.sapsf.service.impl.UserServiceImpl;

public class UserBusinessLogic {

	private UserService userService = new UserServiceImpl();

	public UserDTO userRegistration(UserDTO user) {
		return userService.userRegistration(user);
	}

	public UserDTO userLogin(UserDTO user) {
		return userService.userLogin(user);
	}

	public UserDTO userForgotPassword(UserDTO user) {
		return userService.userForgotPassword(user);
	}
}
