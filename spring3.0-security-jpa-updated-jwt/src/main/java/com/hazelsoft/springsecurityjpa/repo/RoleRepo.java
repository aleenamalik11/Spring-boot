package com.hazelsoft.springsecurityjpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hazelsoft.springsecurityjpa.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Integer> {

	public Role findByName(String name);

}
