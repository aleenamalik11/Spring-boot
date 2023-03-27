package com.hazelsoft.springsecurityjpa.model;

import java.util.List;

public class AuthResponse{

	private String token;

	private String userName;

	private List<String> roleNames;

	public AuthResponse(String token, String userName, List<String> roleNames) {
		this.token = token;
		this.userName = userName;
		this.roleNames = roleNames;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(List<String> roleNames) {
		this.roleNames = roleNames;
	}

	@Override
	public String toString() {
		return "AuthResponse{" +
				"token='" + token + '\'' +
				", userName='" + userName + '\'' +
				", roleNames=" + roleNames +
				'}';
	}
}
