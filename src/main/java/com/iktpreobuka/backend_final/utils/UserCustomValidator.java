package com.iktpreobuka.backend_final.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.iktpreobuka.backend_final.controllers.dto.AdminRegisterDTO;
import com.iktpreobuka.backend_final.controllers.dto.ParentRegisterDTO;
import com.iktpreobuka.backend_final.controllers.dto.StudentRegisterDTO;
import com.iktpreobuka.backend_final.controllers.dto.TeacherRegisterDTO;
import com.iktpreobuka.backend_final.controllers.dto.UserRegisterDTO;

@Component
public class UserCustomValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> myClass) {
		return UserRegisterDTO.class.equals(myClass) 	||
			   AdminRegisterDTO.class.equals(myClass)	||
			   TeacherRegisterDTO.class.equals(myClass) ||
			   ParentRegisterDTO.class.equals(myClass)	||
			   StudentRegisterDTO.class.equals(myClass)	;
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		
		UserRegisterDTO user = (UserRegisterDTO) target;
		
		if (user.getPassword() != null && !user.getPassword().equals(user.getConfirmPassword()))
			errors.reject("400", "Passwords must be the same.");
	}
}
