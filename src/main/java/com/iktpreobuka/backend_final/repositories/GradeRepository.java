package com.iktpreobuka.backend_final.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.backend_final.entities.GradeEntity;

public interface GradeRepository extends CrudRepository<GradeEntity, Integer> {
	
}
