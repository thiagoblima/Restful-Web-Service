package com.udemy.app.ws.service;

import java.util.List;

import com.udemy.app.ws.shared.dto.AddressDto;

public interface AddressService {

	List<AddressDto> getAddresses(String userId);
	AddressDto getUserAddress(String userId, String addressId);
}
