package com.hazelsoft.springsecurityjpa.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.hazelsoft.springsecurityjpa.dto.*;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
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
public class AuthService implements HealthIndicator {

	private AuthenticationManager authenticationManager;
	
	private DataSource datasource;
	
	private UserRepo repository;
	
	private MyUserDetails userDetails;
	
	private JwtService jwtService;
	
	private PasswordEncoder passwordEncoder;
	
	private RoleRepo roleRepository;

	public AuthService(AuthenticationManager authenticationManager, DataSource datasource, 
			UserRepo repository, RoleRepo roleRepository, MyUserDetails userDetails, 
			JwtService jwtService, PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.datasource = datasource;
		this.repository = repository;
		this.roleRepository = roleRepository;
		this.userDetails = userDetails;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
	}

	public AuthResponse register(SignupRequest request) throws SQLException, Exception{
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

		jwt = jwtService.generateToken(userDetails.mapUserToUserDetails(user, userRoles));
		for (Role role : userRoles) {
			roleNameList.add(role.getName());
		}
		return new AuthResponse(jwt, user.getUserName(), roleNameList);
	}

	@Override
	public Health health() {
		try {
			if (datasource.getConnection().isValid(1000)) {
				return Health.up().withDetail(AuthService.class.toString(), "is running as data ")
						.build();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Health.down().withDetail(AuthService.class.toString(), "is down").build();
	}

}
