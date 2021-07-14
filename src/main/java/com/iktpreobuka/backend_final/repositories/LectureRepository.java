package com.iktpreobuka.backend_final.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.backend_final.entities.LectureEntity;
import com.iktpreobuka.backend_final.entities.SubjectEntity;
import com.iktpreobuka.backend_final.entities.TeacherEntity;

public interface LectureRepository extends CrudRepository<LectureEntity, Integer> {
	
	Optional<LectureEntity> findBySubjectAndTeacher(SubjectEntity subject, TeacherEntity teacher);
}
