package com.udemy.app.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.udemy.app.ws.service.AddressService;
import com.udemy.app.ws.service.UserService;
import com.udemy.app.ws.shared.dto.AddressDto;
import com.udemy.app.ws.shared.dto.UserDto;
import com.udemy.app.ws.ui.model.request.UserDetailsRequestModel;
import com.udemy.app.ws.ui.model.response.AddressRest;
import com.udemy.app.ws.ui.model.response.OperationStatusModel;
import com.udemy.app.ws.ui.model.response.RequestOperationName;
import com.udemy.app.ws.ui.model.response.RequestOperationStatus;
import com.udemy.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("users") // http://localhost:8080/users --> http://localhost:8080/mobile-app-ws/users
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "10") int limit) {
		List<UserRest> returnValue = new ArrayList<>();

		List<UserDto> users = userService.getUsers(page, limit);

		for (UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}

		return returnValue;
	}

	@GetMapping(path = "/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserRest getUser(@PathVariable String userId) {
		UserRest returnValue = new UserRest();

		UserDto userDto = userService.getUserByUserId(userId);
		BeanUtils.copyProperties(userDto, returnValue);

		return returnValue;
	}
	
	// http://localhost:8080/mobile-app-ws/users/:userId/addresses
	@GetMapping(path = "/{userId}/addresses", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<AddressRest> getUserAddresses(@PathVariable String userId) {
		List<AddressRest> returnValue = new ArrayList<>();
		List<AddressDto> userAddressesDto = addressService.getAddresses(userId);
		
		if (userAddressesDto != null && !userAddressesDto.isEmpty()) {
			java.lang.reflect.Type listType = new TypeToken<List<AddressRest>>() {}.getType();
			returnValue = new ModelMapper().map(userAddressesDto, listType);
		}
		
		return returnValue;
	}
	
	// http://localhost:8080/mobile-app-ws/users/:userId/addresses/:addressId
	@GetMapping(path = "/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public AddressRest getUserAddress(@PathVariable String userId, @PathVariable String addressId) {
		ModelMapper modelMapper = new ModelMapper();
		
		AddressDto addressDto = addressService.getUserAddress(userId, addressId);
		
		return modelMapper.map(addressDto, AddressRest.class);
	}

	// Framework converts request body (JSON or XML) into UserDetailsRequestModel
	// and viceversa
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		// UserRest returnValue = new UserRest();

//		if (userDetails.getFirstName().isEmpty())
//			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

		//UserDto userDto = new UserDto();
		//BeanUtils.copyProperties(userDetails, userDto); // not a good idea when an Object contains another object
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		UserDto createdUser = userService.createUser(userDto);
		UserRest returnValue = modelMapper.map(createdUser, UserRest.class);

		return returnValue;
	}

	@PutMapping(path = "/{userId}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public UserRest updateUser(@PathVariable String userId, @RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValue = new UserRest();

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto updatedUser = userService.updateUser(userId, userDto);
		BeanUtils.copyProperties(updatedUser, returnValue);

		return returnValue;
	}

	@DeleteMapping(path = "/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String userId) {
		OperationStatusModel returnValue = new OperationStatusModel();

		userService.deleteUser(userId);

		returnValue.setOperationName(RequestOperationName.DELETE.name());
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

		return returnValue;
	}
}
