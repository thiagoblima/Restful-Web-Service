package com.udemy.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udemy.app.ws.io.entity.AddressEntity;
import com.udemy.app.ws.io.entity.UserEntity;
import com.udemy.app.ws.io.repository.UserRepository;
import com.udemy.app.ws.io.repository.AddressRepository;
import com.udemy.app.ws.service.AddressService;
import com.udemy.app.ws.shared.dto.AddressDto;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	@Override
	public List<AddressDto> getAddresses(String userId) {
		ModelMapper modelMapper = new ModelMapper();
		List<AddressDto> returnValue = new ArrayList<>();
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) return returnValue;
		
		Iterable<AddressEntity> addresses = addressRepository.findByUserDetails(userEntity);
		for (AddressEntity addressEntity : addresses) {
			returnValue.add(modelMapper.map(addressEntity, AddressDto.class));
		}
		
		return returnValue;
	}

	@Override
	public AddressDto getUserAddress(String userId, String addressId) {
		ModelMapper modelMapper = new ModelMapper();
		AddressDto returnValue = new AddressDto();
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) return returnValue;
		
		AddressEntity addressEntity = addressRepository.findByUserDetailsAndAddressId(userEntity, addressId);
		
		if (addressEntity != null) {
			return modelMapper.map(addressEntity, AddressDto.class);
		}
		
		return returnValue;
	}

}
