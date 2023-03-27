package com.hazelsoft.springsecurityjpa.controller;

import com.hazelsoft.springsecurityjpa.entity.Role;
import com.hazelsoft.springsecurityjpa.entity.User;
import com.hazelsoft.springsecurityjpa.enums.Status;
import com.hazelsoft.springsecurityjpa.exception.CustomException;
import com.hazelsoft.springsecurityjpa.model.*;
import com.hazelsoft.springsecurityjpa.service.AuthService;
import com.hazelsoft.springsecurityjpa.service.RoleService;
import com.hazelsoft.springsecurityjpa.service.UserService;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin
public class HomeController extends BaseController {
	private RoleService roleService;

	private UserService userService;

	private AuthService authService;

	private RequestResponse requestResponse;

	private String key;
	public HomeController(RoleService roleService,
	  RequestResponse requestResponse, UserService userService, AuthService authService){
		super(requestResponse);
		this.roleService=roleService;
		this.userService=userService;
		this.authService = authService;
		this.requestResponse=requestResponse;
	}
	@GetMapping("/")
	public String home() {
		return ("<h1>Welcome</h1>");
	}

	@PostMapping("/user")
	@PreAuthorize("hasAnyAuthority(@roleService.getAllRoles())")
	public RequestResponse user(@RequestBody CrudUserRequest request) {
		User user = userService.getUserByUserName(request.getUserName());
		return prepareResponse(user, null, "User Page Accessed", Status.SUCCESS);
	}

    @PostMapping("/user/update")
    @PreAuthorize("hasAnyAuthority(@roleService.getAllRoles())")
    public RequestResponse updateUser(@RequestBody User request) {
        User updatedUser = userService.saveOrUpdate(request);
        return prepareResponse(updatedUser, null, "User updated", Status.SUCCESS);
    }
	@PostMapping("/admin")
	@PreAuthorize("hasRole(@roleService.getAdminRole())")
	public RequestResponse admin(@RequestBody PaginationRequest request) {
		Page<User> users = userService.getAllUser
				(PageRequest.of(Integer.parseInt(request.getPage()), request.getSize()));
		return prepareResponse(users, null, "User Page Accessed", Status.SUCCESS);
	}
	@GetMapping("/admin/roles")
	@PreAuthorize("hasAnyAuthority(@roleService.getAdminRole())")
	public RequestResponse getRoles() {
		List<String> roles = roleService.getAllRoles();
		return prepareResponse(roles, null, "User Page Accessed", Status.SUCCESS);
	}
	@PostMapping("/admin/addUser")
	@PreAuthorize("hasRole(@roleService.getAdminRole())")
	public RequestResponse addUser(@RequestBody SignupRequest request) {
		AuthResponse resp = null;
		try{
			resp = authService.register(request);
		}
		 catch (Exception e) {
			 throw new CustomException(e.getMessage());
		}

		if(resp != null) {
			requestResponse = prepareResponse(resp, null, "User saved successfully!", Status.SUCCESS);
		}
		else{
			throw new CustomException("Error occurred while saving the user");
		}
		return requestResponse;
	}

	@PostMapping("/admin/removeUser")
	@PreAuthorize("hasRole(@roleService.getAdminRole())")
	public RequestResponse removeUser(@RequestBody CrudUserRequest request) {
		if(request!=null && request.getUserId()!=null){
			try{
				User user = userService.getUserById(request.getUserId());

				List<Role> roles = user.getRoles();
				roles.clear();
				userService.delete(request.getUserId());
				return prepareResponse(request.getUserId(), null, "User deleted by admin", Status.SUCCESS);

			}
			catch(Exception e){
				throw new CustomException("User does not exist with user id: " + request.getUserId());
			}
		}
		else{
			throw new CustomException("Request is not provided");
		}
	}
}
