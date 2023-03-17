package com.hazelsoft.springsecurityjpa.controller;

import com.hazelsoft.springsecurityjpa.entity.Role;
import com.hazelsoft.springsecurityjpa.entity.User;
import com.hazelsoft.springsecurityjpa.dto.*;
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
public class HomeController extends BaseControllerImpl {
	private RoleService roleService;

	private UserService userService;

	private AuthService authService;

	private RequestResponse requestResponse;

	private String key;
	public HomeController(RoleService roleService, RabbitTemplate rabbitTemplate, DirectExchange directExchange,
						  RequestResponse requestResponse, UserService userService, AuthService authService, String key){
		super(rabbitTemplate, directExchange, requestResponse, key);
		this.roleService=roleService;
		this.userService=userService;
		this.authService = authService;
		this.requestResponse=requestResponse;
	}
	@GetMapping("/")
	public String home() {
		saveActivityAudit("Home Page Accessed by user");
		return ("<h1>Welcome</h1>");
	}

	@PostMapping("/user")
	@PreAuthorize("hasAnyAuthority(@roleService.getAllRoles())")
	public RequestResponse user(@RequestBody CrudUserRequest request) {
		User user = userService.getUserByUserName(request.getUserName());
		saveActivityAudit("User Page Accessed by user");
		return prepareResponse(user, null, "User Page Accessed", Status.SUCCESS);
	}

    @PostMapping("/user/update")
    @PreAuthorize("hasAnyAuthority(@roleService.getAllRoles())")
    public RequestResponse updateUser(@RequestBody User request) {
        User updatedUser = userService.saveOrUpdate(request);
        saveActivityAudit("User info updated by user");
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
		saveActivityAudit("Roles Accessed by admin");
		return prepareResponse(roles, null, "User Page Accessed", Status.SUCCESS);
	}
	@PostMapping("/admin/addUser")
	@PreAuthorize("hasRole(@roleService.getAdminRole())")
	public RequestResponse addUser(@RequestBody SignupRequest request) {
		AuthResponse resp = null;
		try{
			resp = authService.register(request);
		}
		catch (SQLException e){
			requestResponse = prepareResponse(null, e.getStackTrace(), e.getMessage(), Status.ERROR);
			super.saveActivityAudit(e.getMessage() + " while trying to register with userName: " + ((resp!=null)? resp.getUserName(): null));

			return requestResponse;
		} catch (Exception e) {
			requestResponse = prepareResponse(null, e.getStackTrace(), e.getMessage(), Status.ERROR);
			saveActivityAudit(e.getMessage() +" while trying to register with userName: " + ((resp!=null)? resp.getUserName(): null));
			return requestResponse;
		}

		if(resp!=null) {
			requestResponse = prepareResponse(resp, null, "User saved successfully!", Status.SUCCESS);
			saveActivityAudit("User registered with userName: " + ((resp != null) ? resp.getUserName() : null));
		}
		else{
			requestResponse = prepareResponse(resp, null, "User not saved", Status.ERROR);
		}
		return requestResponse;
	}

	@PostMapping("/admin/removeUser")
	@PreAuthorize("hasRole(@roleService.getAdminRole())")
	public RequestResponse removeUser(@RequestBody CrudUserRequest request) {
		if(request!=null && request.getUserId()!=null){
			User user = userService.getUserById(request.getUserId());
			if(user!=null) {
				List<Role> roles = user.getRoles();
				roles.clear();
				userService.delete(request.getUserId());
				saveActivityAudit("User deleted by admin with userName: " + request.getUserName());
				return prepareResponse(request.getUserId(), null, "User deleted by admin", Status.SUCCESS);
			}
		}
		return prepareResponse(null, null, "Couldn't delete user", Status.ERROR);
	}

}
