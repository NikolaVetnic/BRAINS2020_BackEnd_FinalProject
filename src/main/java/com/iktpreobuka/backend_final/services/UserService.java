package com.iktpreobuka.backend_final.services;

import java.util.Optional;

import com.iktpreobuka.backend_final.entities.EUserRole;

public interface UserService {

	public boolean isAuthorizedAs(EUserRole role);
	public Optional<String> getLoggedInUsername();
}
