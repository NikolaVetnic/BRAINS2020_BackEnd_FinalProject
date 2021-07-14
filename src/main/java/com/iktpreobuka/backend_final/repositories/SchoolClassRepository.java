package com.iktpreobuka.backend_final.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.backend_final.entities.SchoolClassEntity;

public interface SchoolClassRepository extends CrudRepository<SchoolClassEntity, Integer> {
	
	Optional<SchoolClassEntity> findByClassNoAndSectionNoAndGeneration(
			Integer classNo, Integer sectionNo, Integer generation);
}
