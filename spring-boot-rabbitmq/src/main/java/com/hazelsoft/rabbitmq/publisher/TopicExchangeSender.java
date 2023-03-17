package com.hazelsoft.rabbitmq.publisher;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class TopicExchangeSender {
	
	private final RabbitTemplate rabbitTemplate;
	
	private final TopicExchange topicExchange;
	
	private final String keyOrderCancel;

	@Value("${prev.key.order}")
	private String prevKeyOrder;


	public TopicExchangeSender(RabbitTemplate rabbitTemplate, TopicExchange topicExchange, 
			String keyOrderCancel,
			String keyOrderProcess, String keyOrderComplete) {
		this.rabbitTemplate = rabbitTemplate;
		this.topicExchange = topicExchange;
		this.keyOrderCancel = keyOrderCancel;

	}

	@GetMapping("/all")
	public String all() {
		rabbitTemplate.convertAndSend(topicExchange.getName(), keyOrderCancel,
				"All orders selected from TopicExchangeSender");
		return "success";
	}
	@GetMapping("/prev")
	public String prev() {
		rabbitTemplate.convertAndSend(topicExchange.getName(), prevKeyOrder, 
				"Previous orders selected from TopicExchangeSender");
		return "success";
	}
}
