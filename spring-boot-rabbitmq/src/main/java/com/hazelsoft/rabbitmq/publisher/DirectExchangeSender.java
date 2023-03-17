package com.hazelsoft.rabbitmq.publisher;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DirectExchangeSender {
	
	private final RabbitTemplate rabbitTemplate;
	
	private final DirectExchange directExchange;
	
	private final String keyOrderCancel;
	
	private final String keyOrderProcess;
	
	private final String keyOrderComplete;


	public DirectExchangeSender(RabbitTemplate rabbitTemplate, DirectExchange directExchange, 
			String keyOrderCancel,
			String keyOrderProcess, String keyOrderComplete) {
		this.rabbitTemplate = rabbitTemplate;
		this.directExchange = directExchange;
		this.keyOrderCancel = keyOrderCancel;
		this.keyOrderProcess = keyOrderProcess;
		this.keyOrderComplete=keyOrderComplete;
	}

	@GetMapping("/cancel")
	public String cancel() {
		rabbitTemplate.convertAndSend(directExchange.getName(), keyOrderCancel, "Order cancelled from DirectExchangeSender" );
		return "success";
	}

	@GetMapping("/process")
	public String process() {
		rabbitTemplate.convertAndSend(directExchange.getName(), keyOrderProcess, "Order processed from DirectExchangeSender");
		return "success";
	}
	@GetMapping("/complete")
	public String complete() {
		rabbitTemplate.convertAndSend(directExchange.getName(), keyOrderComplete, "Order completed from DirectExchangeSender");
		return "success";
	}
}
