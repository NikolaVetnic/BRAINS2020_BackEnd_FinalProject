package com.iktpreobuka.backend_final.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.backend_final.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
	
	Optional<UserEntity> findByUsername(String username);
}
