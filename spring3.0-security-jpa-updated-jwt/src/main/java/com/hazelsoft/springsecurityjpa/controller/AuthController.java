package com.hazelsoft.springsecurityjpa.controller;

import com.hazelsoft.springsecurityjpa.entity.User;
import com.hazelsoft.springsecurityjpa.enums.Status;
import com.hazelsoft.springsecurityjpa.exception.CustomException;
import com.hazelsoft.springsecurityjpa.model.*;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hazelsoft.springsecurityjpa.service.AuthService;


@RestController
@CrossOrigin
public class AuthController extends BaseController{

	private AuthService service;

	private RequestResponse requestResponse;

	private String key;

	public AuthController(AuthService service, RequestResponse requestResponse) {
		super(requestResponse);
		this.service = service;
		this.requestResponse=requestResponse;
	}

	@PostMapping("/register")
	public RequestResponse register(@RequestBody SignupRequest request) {
		AuthResponse resp = null;
		try{
			resp = service.register(request);
		}
		 catch (Exception e) {
			throw new CustomException(e.getMessage());
		}

		requestResponse = prepareResponse(resp, null, "User saved successfully!", Status.SUCCESS);
		return requestResponse;
	}

	@PostMapping("/login")
	public RequestResponse authenticate(@RequestBody LoginRequest request)  {
		AuthResponse  resp = null;
		try{
			resp = service.authenticate(request);
		}
		 catch (Exception e) {
			 throw new CustomException(e.getMessage());
		}

		return prepareResponse(resp, null, "User logged-in successfully!", Status.SUCCESS);
	}

}