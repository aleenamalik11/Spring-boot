package com.hazelsoft.springsecurityjpa.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hazelsoft.springsecurityjpa.entity.Role;
import com.hazelsoft.springsecurityjpa.entity.User;
import com.hazelsoft.springsecurityjpa.dto.MyUserDetails;

@Service
public class MyUserDetailsService implements UserDetailsService {

	private UserService userService;

	public MyUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		User user = userService.getUserByUserName(userName);
		List<Role> userRoles = userService.getUserRoles(user.getId());
		MyUserDetails myUserDetails = new MyUserDetails();
		myUserDetails.mapUserToUserDetails(user, userRoles);

		return myUserDetails;
	}
}
