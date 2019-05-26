package com.udemy.app.ws.io.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.udemy.app.ws.io.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

	UserEntity findUserByEmail(String email); // Spring Data JPA example
}
