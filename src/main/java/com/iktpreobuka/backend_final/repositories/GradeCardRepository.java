package com.iktpreobuka.backend_final.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.backend_final.entities.GradeCardEntity;
import com.iktpreobuka.backend_final.entities.LectureEntity;
import com.iktpreobuka.backend_final.entities.StudentEntity;

public interface GradeCardRepository extends CrudRepository<GradeCardEntity, Integer> {
	
	Optional<GradeCardEntity> findByLectureAndStudent(LectureEntity lecture, StudentEntity student);
}
