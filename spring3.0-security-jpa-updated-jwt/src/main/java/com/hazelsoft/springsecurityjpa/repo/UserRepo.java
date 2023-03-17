package com.hazelsoft.springsecurityjpa.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hazelsoft.springsecurityjpa.entity.User;
import com.hazelsoft.springsecurityjpa.entity.Role;

public interface UserRepo extends JpaRepository<User, Integer> {

	public User findByUserName(String username);

	@Query("SELECT r FROM User u INNER JOIN u.roles r WHERE u.id = :id")
	public List<Role> getUserRoles(@Param("id") int id);


	public Page<User> findAll(Pageable pageable);


}
