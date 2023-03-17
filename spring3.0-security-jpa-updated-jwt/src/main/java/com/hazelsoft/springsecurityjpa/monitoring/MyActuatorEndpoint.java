package com.hazelsoft.springsecurityjpa.monitoring;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "myEndpoint")
public class MyActuatorEndpoint {

	private Map<String, String> functions = new HashMap<>();

	@ReadOperation
	public Map<String, String> functions() {
		functions.put("My Endpoint", "Read Op");
		return functions;
	}

	@ReadOperation
	public String feature(@Selector String name) {
		return functions.get(name);
	}

	@WriteOperation
	public void configureFeature(@Selector String name, String function) {
		functions.put(name, function);
	}

	@DeleteOperation
	public void deleteFeature(@Selector String name) {
		functions.remove(name);
	}
}
