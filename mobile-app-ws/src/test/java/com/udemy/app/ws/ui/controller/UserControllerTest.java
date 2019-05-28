package com.udemy.app.ws.ui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.udemy.app.ws.service.UserService;
import com.udemy.app.ws.shared.dto.AddressDto;
import com.udemy.app.ws.shared.dto.UserDto;
import com.udemy.app.ws.ui.model.response.UserRest;

class UserControllerTest {
	
	/*
	 * TESTING CLASS 
	 */
	
	@InjectMocks
	UserController userController;
	
	
	/*
	 * MOCK CLASSES 
	 */

	@Mock
	UserService userService;

	/*
	 * ATRIBUTES 
	 */
	
	final String USER_ID = "SDAQ43QEwads21QWSAD312Easdw";
	
	UserDto userDto = new UserDto();
	
	/*
	 * METHODS 
	 */

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		userDto.setId(1L);
		userDto.setUserId(USER_ID);
		userDto.setFirstName("Yevon");
		userDto.setLastName("Allo");
		userDto.setEmail("verified@done.com");
		userDto.setEncryptedPassword("sadasdas4132t4wrsfdwq");
		userDto.setEmailVerificationToken("daszxdse4rtdgferw432efdsaq");
		userDto.setEmailVerificationStatus(true);
		userDto.setAddresses(getAddressesDto());
	}

	@Test
	void testGetUser() {
		when(userService.getUserByUserId(anyString())).thenReturn(userDto);
		
		UserRest userRest = userController.getUser(USER_ID);
		
		assertNotNull(userRest);
		assertEquals(USER_ID, userRest.getUserId());
		assertEquals(userDto.getFirstName(), userRest.getFirstName());
		assertEquals(userDto.getLastName(), userRest.getLastName());
		assertTrue(userDto.getAddresses().size() == userRest.getAddresses().size());
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

}
