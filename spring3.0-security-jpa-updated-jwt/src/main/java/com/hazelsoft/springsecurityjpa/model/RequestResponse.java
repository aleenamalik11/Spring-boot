package com.hazelsoft.springsecurityjpa.model;

import com.hazelsoft.springsecurityjpa.enums.Status;


public class RequestResponse<T, W> {

	private Status status;

	private String message;

	private T payload;

	private W errors;

	public RequestResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RequestResponse(Status status, String message, T payload, W errors) {
		super();
		this.status = status;
		this.message = message;
		this.payload = payload;
		this.errors = errors;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getPayload() {
		return payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}

	public Object getErrors() {
		return errors;
	}

	public void setErrors(W errors) {
		this.errors = errors;
	}

	@Override
	public String toString() {
		return "RequestResponse [status=" + status + ", message=" + message + ", payload=" + payload + ", errors="
				+ errors + "]";
	}

}
