package com.udemy.app.ws.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.udemy.app.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {

	UserDto getUser(String email);
	UserDto getUserByUserId(String userId);
	UserDto createUser(UserDto user);
	UserDto updateUser(String userId, UserDto user);
	void deleteUser(String userId);
}
