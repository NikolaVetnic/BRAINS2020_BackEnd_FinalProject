package com.iktpreobuka.backend_final.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.backend_final.entities.AdminEntity;

public interface AdminRepository extends CrudRepository<AdminEntity, Integer> {
	
}
