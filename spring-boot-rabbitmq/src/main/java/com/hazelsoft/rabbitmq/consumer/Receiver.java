package com.hazelsoft.rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
	@RabbitListener(queues = "${queue.order.complete}")
	public void receiver1(String message) {
		System.out.println("Received message: " + message + " from " + "{queue-order-complete}"); 
	}
	@RabbitListener(queues = "${queue.order.cancel}")
	public void receiver2(String message) {
		System.out.println("Received message: " + message + " from " + "{queue-order-cancel}");
	}
	@RabbitListener(queues = "${queue.order.process}")
	public void receiver3(String message) {
		System.out.println("Received message: " + message + " from " + "{queue-order-process}");
	}
}