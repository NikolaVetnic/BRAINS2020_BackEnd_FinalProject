package com.iktpreobuka.backend_final.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.iktpreobuka.backend_final.entities.EUserRole;
import com.iktpreobuka.backend_final.entities.UserEntity;
import com.iktpreobuka.backend_final.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean isAuthorizedAs(EUserRole role) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    
		    UserEntity currentUser = userRepository.findByUsername(authentication.getName()).get();
		    return currentUser.getRole() == role;
		}
		
		return false;
	}

	@Override
	public Optional<String> getLoggedInUsername() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String loggedInUsername = null;
		
		if (!(authentication instanceof AnonymousAuthenticationToken))
			loggedInUsername = authentication.getName();
		
		return Optional.ofNullable(loggedInUsername);
	}
}
