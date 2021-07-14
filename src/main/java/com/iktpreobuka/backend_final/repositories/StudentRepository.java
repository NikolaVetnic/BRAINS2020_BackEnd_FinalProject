package com.iktpreobuka.backend_final.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.backend_final.entities.StudentEntity;

public interface StudentRepository extends CrudRepository<StudentEntity, Integer> {
	
	Optional<StudentEntity> findByJmbg(String jmbg);
}
