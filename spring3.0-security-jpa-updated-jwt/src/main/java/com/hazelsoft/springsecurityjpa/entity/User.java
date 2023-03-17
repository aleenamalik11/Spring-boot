package com.hazelsoft.springsecurityjpa.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;

import jakarta.persistence.*;

@Entity
@Table(name = "users",  uniqueConstraints = {
		@UniqueConstraint(columnNames = {"user_name"})})
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String name;
	
	@Column(name = "user_name", unique = true)
	@NonNull
	private String userName;
	
	@NonNull
	private String password;
	
	private String city;	

	@ManyToMany(cascade = {CascadeType.ALL , CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), 
	inverseJoinColumns = @JoinColumn(name = "role_id"))
	@NonNull
	private List<Role> roles = new ArrayList<>();
	

	public User() {
		super();
	}

	public User(String name, String city, List<Role> roles) {
		super();
		this.name = name;
		this.city = city;
		this.roles = roles;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", userName=" + userName + ", "
				+ "password=" + password + ", city="
				+ city + ", roles=" + roles + "]";
	}
}
