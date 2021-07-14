package com.iktpreobuka.backend_final.utils;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.iktpreobuka.backend_final.controllers.dto.SchoolClassRegisterDTO;
import com.iktpreobuka.backend_final.entities.SchoolClassEntity;
import com.iktpreobuka.backend_final.repositories.SchoolClassRepository;

@Component
public class SchoolClassCustomValidator implements Validator {
	
	@Autowired SchoolClassRepository schoolClassRepository;
	
	@Override
	public boolean supports(Class<?> myClass) {
		return SchoolClassRegisterDTO.class.equals(myClass);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		
		SchoolClassRegisterDTO schoolClassDTO = (SchoolClassRegisterDTO) target;
		
		Optional<SchoolClassEntity> result = schoolClassRepository.findByClassNoAndSectionNoAndGeneration(
				schoolClassDTO.getClassNo(), schoolClassDTO.getSectionNo(), schoolClassDTO.getGeneration());
		
		if (result.isPresent())
			errors.reject("400", "Such class already exists.");
	}
}
