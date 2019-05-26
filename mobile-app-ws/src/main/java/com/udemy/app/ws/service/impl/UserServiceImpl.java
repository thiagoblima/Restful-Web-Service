package com.udemy.app.ws.service.impl;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.udemy.app.ws.io.entity.UserEntity;
import com.udemy.app.ws.io.repository.UserRepository;
import com.udemy.app.ws.service.UserService;
import com.udemy.app.ws.shared.Utils;
import com.udemy.app.ws.shared.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto createUser(UserDto user) {
		if (userRepository.findUserByEmail(user.getEmail()) != null)
			throw new RuntimeException("Record already exists");

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		String publicUserId = utils.generateUserId(30);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setUserId(publicUserId);

		UserEntity storedUserDetails = userRepository.save(userEntity);
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, returnValue);

		return returnValue;
	}

	@Override
	public UserDto getUser(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findUserByEmail(email);
		
		if (userEntity == null)
			throw new UsernameNotFoundException(email);
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}

	// Method that helps spring to load user details when it needs it.
	// This method will be used in the sign in process: User will send
	// a UserDetails with username(email) and password and spring will
	// look for a user with this values.
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findUserByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);

		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

}
