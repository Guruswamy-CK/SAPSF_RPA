package com.rpa.sapsf.service;

import com.rpa.sapsf.dto.UserDTO;

public interface UserService {
	UserDTO userRegistration(UserDTO user);

	UserDTO userLogin(UserDTO user);

	UserDTO userForgotPassword(UserDTO user);
}
