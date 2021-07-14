package com.iktpreobuka.backend_final.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.backend_final.entities.ParentEntity;

public interface ParentRepository extends CrudRepository<ParentEntity, Integer> {
	
	Optional<ParentEntity> findByEmail(String email);
}
