package com.iktpreobuka.backend_final.controllers;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.backend_final.controllers.dto.SchoolClassRegisterDTO;
import com.iktpreobuka.backend_final.controllers.util.RESTError;
import com.iktpreobuka.backend_final.entities.EUserRole;
import com.iktpreobuka.backend_final.entities.SchoolClassEntity;
import com.iktpreobuka.backend_final.entities.StudentEntity;
import com.iktpreobuka.backend_final.repositories.SchoolClassRepository;
import com.iktpreobuka.backend_final.repositories.StudentRepository;
import com.iktpreobuka.backend_final.services.UserServiceImpl;
import com.iktpreobuka.backend_final.utils.SchoolClassCustomValidator;

@RestController
@RequestMapping(path = "/api/v1/project/classes")
public class SchoolClassController {
	
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	
	@Autowired SchoolClassRepository schoolClassRepository;
	@Autowired StudentRepository studentRepository;

	@Autowired private UserServiceImpl userServiceImpl;
	
	@Autowired SchoolClassCustomValidator schoolClassValidator;

	
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.addValidators(schoolClassValidator);
	}
	
	
	// =-=-=-= POST =-=-=-=
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/register")
	public ResponseEntity<?> addNewSchoolClass(@Valid @RequestBody SchoolClassRegisterDTO schoolClassDTO, BindingResult result) {
		
		if (!userServiceImpl.isAuthorizedAs(EUserRole.ADMIN))
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized request."), HttpStatus.UNAUTHORIZED);
		
		if (result.hasErrors())
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		else
			schoolClassValidator.validate(schoolClassDTO, result);
		
		SchoolClassEntity schoolClass = new SchoolClassEntity();
		
		schoolClass.setClassNo(schoolClassDTO.getClassNo());
		schoolClass.setSectionNo(schoolClassDTO.getSectionNo());
		schoolClass.setGeneration(schoolClassDTO.getGeneration());
		
		schoolClassRepository.save(schoolClass);
		
		logger.info(userServiceImpl.getLoggedInUsername() + " : registered class " + 
				schoolClass.getClassNo() + "-" + schoolClass.getSectionNo());
		
		return new ResponseEntity<>(schoolClass, HttpStatus.OK);
	}
	
	
	// =-=-=-= PUT =-=-=-=
	
	
	@RequestMapping(method = RequestMethod.PUT, value = "/insert/{studentId}/into/{schoolClassId}")
	public ResponseEntity<?> connectStudentWithClass(@PathVariable Integer studentId, @PathVariable Integer schoolClassId) {
		
		if (!userServiceImpl.isAuthorizedAs(EUserRole.ADMIN))
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized request."), HttpStatus.UNAUTHORIZED);
		
		try {
		
			Optional<StudentEntity> studentOpt = studentRepository.findById(studentId);
			Optional<SchoolClassEntity> schoolClassOpt = schoolClassRepository.findById(schoolClassId);
			
			if (!studentOpt.isPresent())
				return new ResponseEntity<RESTError>(
						new RESTError(HttpStatus.NOT_FOUND.value(), "Student not found."), HttpStatus.NOT_FOUND);
			
			if (!schoolClassOpt.isPresent())
				return new ResponseEntity<RESTError>(
						new RESTError(HttpStatus.NOT_FOUND.value(), "Class not found."), HttpStatus.NOT_FOUND);
			
			StudentEntity student = studentOpt.get();
			SchoolClassEntity schoolClass = schoolClassOpt.get();
			
			student.setSchoolClass(schoolClass);
			studentRepository.save(student);
			
			schoolClass.getStudents().add(student);
			schoolClassRepository.save(schoolClass);
			
			logger.info(userServiceImpl.getLoggedInUsername() + " : added student " + student.getUsername() + 
					" to class " + schoolClass.getClassNo() + "-" + schoolClass.getSectionNo());
			
			return new ResponseEntity<>(student, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
							"Internal server error. Error: " + e.getMessage()), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));
	}
}
