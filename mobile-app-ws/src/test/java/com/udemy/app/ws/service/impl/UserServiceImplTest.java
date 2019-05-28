package com.udemy.app.ws.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.udemy.app.ws.exceptions.UserServiceException;
import com.udemy.app.ws.io.entity.AddressEntity;
import com.udemy.app.ws.io.entity.UserEntity;
import com.udemy.app.ws.io.repository.UserRepository;
import com.udemy.app.ws.shared.Utils;
import com.udemy.app.ws.shared.dto.AddressDto;
import com.udemy.app.ws.shared.dto.UserDto;

class UserServiceImplTest {

	/*
	 * TESTING CLASS 
	 */
	
	// InjectMocks annotation is like Autowire but it inject also the mocks
	// defined below which the class need to be tested
	@InjectMocks
	UserServiceImpl userService;
	
	
	/*
	 * MOCK CLASSES 
	 */
	
	// Mock object is kind of fake class which we can instantiate and fake the
	// return results their real methods return
	@Mock
	UserRepository userRepository;
	
	@Mock
	Utils utils;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	/*
	 * ATRIBUTES 
	 */
	
	String userId = "dsakm2153962kmdakowe321";
	String encryptedPassword = "jdas56432jeqw5thn12fonjh98s3";
	UserEntity userEntity = new UserEntity();
	
	/*
	 * METHODS 
	 */

	// Executed before each test case
	@BeforeEach
	void setUp() throws Exception {
		// Mockito will instantiate mocks
		MockitoAnnotations.initMocks(this);
		
		userEntity.setId(1L);
		userEntity.setFirstName("Saul");
		userEntity.setLastName("Koper");
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(encryptedPassword);
		userEntity.setEmail("helloWorld@moon.com");
		userEntity.setAddresses(getAddressesEntity());
	}

	@Test
	void testGetUser() {
		when(userRepository.findUserByEmail(anyString())).thenReturn(userEntity);
		
		UserDto userDto = userService.getUser("test@test.com");
		
		assertNotNull(userDto);
		assertEquals("Saul", userDto.getFirstName());
	}
	
	@Test
	void testGetUser_UsernameNotFoundException() {
		when(userRepository.findUserByEmail(anyString())).thenReturn(null);
		
		assertThrows(UsernameNotFoundException.class,
				
				() -> {
					userService.getUser("test@test.com");			
				}
		);
	}

	@Test
	void testCreateUser() {
		when(userRepository.findUserByEmail(anyString())).thenReturn(null);
		when(utils.generateAddressId(anyInt())).thenReturn("sa542iu758eqrdslkf7yqno");
		when(utils.generateUserId(anyInt())).thenReturn(userId);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		
		// With this notation, createUser will not call the method save of UserRepository
		// when the code is being tested.
		// Mockito.doNothing().when(userRepository).save(any(UserEntity.class));
		
		UserDto userDto = new UserDto();
		userDto.setAddresses(getAddressesDto());
		userDto.setFirstName("Cacuna");
		userDto.setLastName("Matata");
		userDto.setPassword("hello123");
		userDto.setEmail("green.action@restserver.com");
		
		UserDto createdUserDetails = userService.createUser(userDto);
		assertNotNull(createdUserDetails);
		assertNotNull(createdUserDetails.getUserId());
		assertEquals(userEntity.getFirstName(), createdUserDetails.getFirstName());
		assertEquals(userEntity.getLastName(), createdUserDetails.getLastName());
		assertEquals(createdUserDetails.getAddresses().size(), userEntity.getAddresses().size());
		verify(utils, times(createdUserDetails.getAddresses().size())).generateAddressId(30); // Method generateAddressId need to be called twice
		verify(bCryptPasswordEncoder, times(1)).encode("hello123");
		verify(utils, times(1)).generateUserId(30);
		verify(userRepository, times(1)).save(any(UserEntity.class));
	}
	
	@Test
	void testCreateUser_UserServiceException() {
		when(userRepository.findUserByEmail(anyString())).thenReturn(userEntity);
		
		UserDto userDto = new UserDto();
		userDto.setAddresses(getAddressesDto());
		userDto.setFirstName("Cacuna");
		userDto.setLastName("Matata");
		userDto.setPassword("hello123");
		userDto.setEmail("green.action@restserver.com");
		
		assertThrows(UserServiceException.class,
				
				() -> {
					userService.createUser(userDto);			
				}
		);
	}
	
	private List<AddressDto> getAddressesDto() {
		// Try to add as many fields as real scenario would have
		AddressDto addressDto = new AddressDto();
		addressDto.setType("shipping");
		addressDto.setCity("Paris");
		addressDto.setCountry("France");
		addressDto.setPostalCode("098765");
		addressDto.setStreetName("Angels in Rome");
		
		AddressDto billingAddressDto = new AddressDto();
		billingAddressDto.setType("billing");
		billingAddressDto.setCity("Paris");
		billingAddressDto.setCountry("France");
		billingAddressDto.setPostalCode("098765");
		billingAddressDto.setStreetName("Angels in Rome");
		
		List<AddressDto> addresses = new ArrayList<>();
		addresses.add(addressDto);
		addresses.add(billingAddressDto);
		
		return addresses;
	}
	
	private List<AddressEntity> getAddressesEntity() {
		List<AddressDto> addresses = getAddressesDto();
		
		Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
		return new ModelMapper().map(addresses, listType);
	}
}
