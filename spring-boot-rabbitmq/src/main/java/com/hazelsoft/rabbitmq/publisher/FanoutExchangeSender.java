package com.hazelsoft.rabbitmq.publisher;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FanoutExchangeSender {
	
	private final RabbitTemplate rabbitTemplate;
	
	private final FanoutExchange fanoutExchange;

	public FanoutExchangeSender(RabbitTemplate rabbitTemplate, FanoutExchange fanoutExchange) {
		this.rabbitTemplate = rabbitTemplate;
		this.fanoutExchange = fanoutExchange;
	}

	@GetMapping("/fan")
	public String send() {
		rabbitTemplate.convertAndSend(fanoutExchange.getName(), "", "Fanout message");
		return "success";
	}
}
