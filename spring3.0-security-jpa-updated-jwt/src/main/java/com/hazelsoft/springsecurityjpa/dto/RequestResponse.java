package com.hazelsoft.springsecurityjpa.dto;

public class RequestResponse {

	private Status status;

	private String message;

	private Object payload;

	private Object errors;

	public RequestResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RequestResponse(Status status, String message, Object payload, Object errors) {
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

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public Object getErrors() {
		return errors;
	}

	public void setErrors(Object errors) {
		this.errors = errors;
	}

	@Override
	public String toString() {
		return "RequestResponse [status=" + status + ", message=" + message + ", payload=" + payload + ", errors="
				+ errors + "]";
	}

}
