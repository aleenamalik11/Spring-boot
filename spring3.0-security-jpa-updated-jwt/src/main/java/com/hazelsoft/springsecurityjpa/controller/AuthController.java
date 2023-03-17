package com.hazelsoft.springsecurityjpa.controller;

import com.hazelsoft.springsecurityjpa.dto.*;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hazelsoft.springsecurityjpa.service.AuthService;

import java.sql.SQLException;

@RestController
@CrossOrigin
public class AuthController extends BaseControllerImpl{
	
	private AuthService service;

	private RequestResponse requestResponse;

	private String key;

	public AuthController(AuthService service, RabbitTemplate rabbitTemplate, DirectExchange directExchange,
						  RequestResponse requestResponse, String key) {
		super(rabbitTemplate, directExchange, requestResponse, key);
		this.service = service;
		this.requestResponse=requestResponse;
	}

	@PostMapping("/register")
	public RequestResponse register(@RequestBody SignupRequest request) {
		AuthResponse resp = null;
		try{
			resp = service.register(request);
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

		requestResponse = prepareResponse(resp, null, "User saved successfully!", Status.SUCCESS);
		saveActivityAudit("User registered with userName: " + ((resp!=null)? resp.getUserName(): null));
		return requestResponse;
	}

	@PostMapping("/login")
	public RequestResponse authenticate(@RequestBody LoginRequest request)  {
		AuthResponse  resp = null;
		try{
			resp = service.authenticate(request);
		}
		catch (SQLException e){
			requestResponse = prepareResponse(null, e.getStackTrace(), e.getMessage(), Status.ERROR);
			saveActivityAudit(e.getMessage() + " while trying to login with userName: " + ((resp!=null)? resp.getUserName(): null));
			return requestResponse;
		} catch (Exception e) {
			requestResponse = prepareResponse(null, e.getStackTrace(), e.getMessage(), Status.ERROR);
			saveActivityAudit(e.getMessage() + " while trying to login with userName: " + ((resp!=null)? resp.getUserName(): null));
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

}
