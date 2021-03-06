package com.iktpreobuka.backend_final.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.backend_final.controllers.dto.AdminRegisterDTO;
import com.iktpreobuka.backend_final.controllers.util.RESTError;
import com.iktpreobuka.backend_final.entities.AdminEntity;
import com.iktpreobuka.backend_final.entities.EUserRole;
import com.iktpreobuka.backend_final.repositories.AdminRepository;
import com.iktpreobuka.backend_final.repositories.UserRepository;
import com.iktpreobuka.backend_final.services.UserServiceImpl;
import com.iktpreobuka.backend_final.utils.UserCustomValidator;

@RestController
@RequestMapping(path = "/api/v1/project/admin")
public class AdminController {
	
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	
	@Autowired private AdminRepository adminRepository;
	@Autowired private UserRepository userRepository;

	@Autowired private UserServiceImpl userServiceImpl;
	
	@Autowired UserCustomValidator userValidator;
	
	
	// =-=-=-= GET =-=-=-=
	
	
	@RequestMapping(path = "/users", method = RequestMethod.GET) 
	public ResponseEntity<?> getAllUsers() {
		
		if (!userServiceImpl.isAuthorizedAs(EUserRole.ADMIN))
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized request."), HttpStatus.UNAUTHORIZED);
		
		logger.info(userServiceImpl.getLoggedInUsername() + " : viewed all users.");

		return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
	}
	
	
	@RequestMapping(path = "/logs", method = RequestMethod.GET)
	public ResponseEntity<?> getLogs() throws IOException {
		
		if (!userServiceImpl.isAuthorizedAs(EUserRole.ADMIN))
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized request."), HttpStatus.UNAUTHORIZED);
		
		BufferedReader in = new BufferedReader(
				new FileReader("logs//spring-boot-logging.log"));
		
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = in.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		
		logger.info(userServiceImpl.getLoggedInUsername() + " : viewed logs.");
		
		return new ResponseEntity<>(sb.toString(), HttpStatus.OK);
	}
	
	
	// =-=-=-= PUT =-=-=-=
	
	
	@RequestMapping(method = RequestMethod.PUT, value = "/update/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody AdminRegisterDTO adminDTO, BindingResult result) {

		if (!userServiceImpl.isAuthorizedAs(EUserRole.ADMIN))
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized request."), HttpStatus.UNAUTHORIZED);
		
		if (result.hasErrors())
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		else
			userValidator.validate(adminDTO, result);
		
		Optional<AdminEntity> adminOpt = adminRepository.findById(id);
		
		if (!adminOpt.isPresent())
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(), "Subject not found."), HttpStatus.NOT_FOUND);
		
		AdminEntity admin = adminOpt.get();
		
		admin.setUsername(adminDTO.getUsername());
		admin.setPassword(adminDTO.getPassword());
		
		adminRepository.save(admin);
		
		logger.info(userServiceImpl.getLoggedInUsername() + " : updated admin " + admin.getUsername());
		
		return new ResponseEntity<>(admin, HttpStatus.OK);
	}
	

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));
	}
	
	
	// =-=-=-= DELETE =-=-=-=
	
	
	@RequestMapping(method = RequestMethod.DELETE, value ="/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		
		if (!userServiceImpl.isAuthorizedAs(EUserRole.ADMIN))
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized request."), HttpStatus.UNAUTHORIZED);
		
		try {
		
			AdminEntity admin = adminRepository.findById(id).orElse(null);
			
			adminRepository.delete(admin);
			
			if (admin == null)
				return new ResponseEntity<RESTError>(
						new RESTError(HttpStatus.NOT_FOUND.value(), "Admin not found."), HttpStatus.NOT_FOUND);
			
			logger.info(userServiceImpl.getLoggedInUsername() + " : deleted admin " + admin.getUsername());
			
			return new ResponseEntity<AdminEntity>(admin, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error. Error: " + e.getMessage()), 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
