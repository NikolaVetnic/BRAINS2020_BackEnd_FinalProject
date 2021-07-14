package com.iktpreobuka.backend_final.utils;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.iktpreobuka.backend_final.controllers.dto.SubjectRegisterDTO;
import com.iktpreobuka.backend_final.entities.SubjectEntity;
import com.iktpreobuka.backend_final.repositories.SubjectRepository;

@Component
public class SubjectCustomValidator implements Validator {
	
	@Autowired SubjectRepository subjectRepository;
	
	@Override
	public boolean supports(Class<?> myClass) {
		return SubjectRegisterDTO.class.equals(myClass);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		
		SubjectRegisterDTO subjectDTO = (SubjectRegisterDTO) target;
		
		Optional<SubjectEntity> result = subjectRepository.findByNameAndYearAccredited(
				subjectDTO.getName(), subjectDTO.getYearAccredited());
		
		if (result.isPresent())
			errors.reject("400", "Such subject already exists.");
	}
}
