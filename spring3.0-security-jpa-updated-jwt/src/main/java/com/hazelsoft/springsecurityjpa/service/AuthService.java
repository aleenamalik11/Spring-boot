package com.hazelsoft.springsecurityjpa.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hazelsoft.springsecurityjpa.model.*;
import com.hazelsoft.springsecurityjpa.rabbitmq.Publisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hazelsoft.springsecurityjpa.entity.Role;
import com.hazelsoft.springsecurityjpa.entity.User;
import com.hazelsoft.springsecurityjpa.repo.RoleRepo;
import com.hazelsoft.springsecurityjpa.repo.UserRepo;

@Service
public class AuthService {

	private AuthenticationManager authenticationManager;

	private UserRepo repository;
	
	private MyUserDetails userDetails;
	
	private JwtService jwtService;
	
	private PasswordEncoder passwordEncoder;
	
	private RoleRepo roleRepository;

	private Publisher publisher;

	public AuthService(AuthenticationManager authenticationManager,
					   UserRepo repository, RoleRepo roleRepository, MyUserDetails userDetails,
					   JwtService jwtService, PasswordEncoder passwordEncoder, Publisher publisher) {
		this.authenticationManager = authenticationManager;
		this.repository = repository;
		this.roleRepository = roleRepository;
		this.userDetails = userDetails;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
		this.publisher = publisher;
	}

	public AuthResponse register(SignupRequest request) throws  Exception{
		String jwt = null;
		User savedUser = null;
		User user = new User();
		List<String> roleNameList = new ArrayList<>();
		List<Role> roles = new ArrayList<>();

		user.setName(request.getName());
		user.setUserName(request.getUserName());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setCity(request.getCity());

		for (Role role : request.getRoles()) {
			Role existingRole = roleRepository.findByName(role.getName());
			if (existingRole != null) {
				roles.add(existingRole);
			} else {
				Role newRole = roleRepository.save(role);
				roles.add(newRole);
			}
		}
		user.setRoles(roles);

		savedUser = repository.save(user);
		if(savedUser!=null) {
			publisher.saveActivityAudit("User saved successfully  with user name "
					+ savedUser.getUserName());
			for (Role role : savedUser.getRoles()) {
				roleNameList.add(role.getName());
			}
			jwt = jwtService.generateToken(userDetails.mapUserToUserDetails(savedUser,
					savedUser.getRoles()));

			return new AuthResponse(jwt, user.getUserName(), roleNameList);
		}
		return new AuthResponse(null, null, null);
	}

	public AuthResponse authenticate(LoginRequest request) throws SQLException,
			Exception{
		String jwt = null;
		User user = null;
		List<Role> userRoles = null;
		List<String> roleNameList = new ArrayList<>();
		Authentication authentication = null;

		authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUserName(),
						request.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		user = repository.findByUserName(request.getUserName());
		userRoles = repository.getUserRoles(user.getId());
		publisher.saveActivityAudit("User logged-in successfully  with user name "
				+ user.getUserName());
		jwt = jwtService.generateToken(userDetails.mapUserToUserDetails(user, userRoles));
		for (Role role : userRoles) {
			roleNameList.add(role.getName());
		}
		return new AuthResponse(jwt, user.getUserName(), roleNameList);
	}

}
