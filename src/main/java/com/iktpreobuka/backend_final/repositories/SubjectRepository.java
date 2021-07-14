package com.iktpreobuka.backend_final.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.backend_final.entities.SubjectEntity;

public interface SubjectRepository extends CrudRepository<SubjectEntity, Integer> {
	
	Optional<SubjectEntity> findByNameAndYearAccredited(String name, Integer yearAccredited);
}
