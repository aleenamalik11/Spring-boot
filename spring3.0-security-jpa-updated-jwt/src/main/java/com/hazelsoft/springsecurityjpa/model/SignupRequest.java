package com.hazelsoft.springsecurityjpa.model;

import java.util.List;

import com.hazelsoft.springsecurityjpa.entity.Role;

public class SignupRequest {
	
	private String name;
	
	private String userName;
	
	private String password;
	
	private String city;
	
	private List<Role> roles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "SignupRequest [name=" + name + ", userName=" + userName + ", password=" + password + ", city=" + city
				+ ", roles=" + roles + "]";
	}

}
