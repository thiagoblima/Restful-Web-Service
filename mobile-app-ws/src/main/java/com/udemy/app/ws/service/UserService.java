package com.udemy.app.ws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.udemy.app.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {

	List<UserDto> getUsers(int page, int limit);
	UserDto getUser(String email);
	UserDto getUserByUserId(String userId);
	UserDto createUser(UserDto user);
	UserDto updateUser(String userId, UserDto user);
	void deleteUser(String userId);
}
