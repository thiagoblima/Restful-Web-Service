package com.udemy.app.ws.io.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.udemy.app.ws.io.entity.AddressEntity;
import com.udemy.app.ws.io.entity.UserEntity;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {

	List<AddressEntity> findByUserDetails(UserEntity userEntity);
	AddressEntity findByUserDetailsAndAddressId(UserEntity userEntity, String addressId);
}
