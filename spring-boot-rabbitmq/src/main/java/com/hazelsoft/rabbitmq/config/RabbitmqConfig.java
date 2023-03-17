package com.hazelsoft.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@EnableRabbit
@Configuration
public class RabbitmqConfig {
	@Value("${queue.order.cancel}")
	private String QueueCancelledOrders;
	
	@Value("${queue.order.process}")
	private String QueueProcessedOrders;

	@Value("${queue.order.complete}")
	private String QueueCompletedOrders;

	@Value("${direct.order.exchange}")
	private String directOrderExchange;

	@Value("${topic.order.exchange}")
	private String topicOrderExchange;

	@Value("${fanout.order.exchange}")
	private String fanoutOrderExchange;
	@Value("${header.order.exchange}")
	private String headerOrderExchange;
	@Value("${key.order.cancel}")
	private String keyOrderCancel;
	
	@Value("${key.order.process}")
	private String keyOrderProcess;

	@Value("${key.order.complete}")
	private String keyOrderComplete;
	
	@Value("${key.order}")
	private String keyOrder;
	
	@Value("${previous.key.order}")
	private String previousKeyOrder;

	// Creating  queues
	@Bean
	public Queue QueueCancelledOrders() {
		return new Queue(QueueCancelledOrders);
	}

	@Bean
	public Queue QueueCompletedOrders() {
		return new Queue(QueueCompletedOrders);
	}
	
	@Bean
	public Queue QueueProcessedOrders() {
		return new Queue(QueueProcessedOrders);
	}
	
	//creating exchanges
	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange(directOrderExchange);
	}
	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange(topicOrderExchange);
	}
	@Bean
	public FanoutExchange fanoutExchange() {
		return new FanoutExchange(fanoutOrderExchange);
	}

	@Bean
	public HeadersExchange headersExchange() {
		return new HeadersExchange(headerOrderExchange);
	}

	
	// key beans	
	@Bean
	public String keyOrderCancel() {
		return keyOrderCancel;
	}
	@Bean
	public String keyOrderProcess() {
		return keyOrderProcess;
	}
	@Bean
	public String keyOrderComplete() {
		return keyOrderComplete;
	}
	@Bean
	public String keyOrder() {
		return keyOrder;
	}
	@Bean
	public String previousKeyOrder() {
		return previousKeyOrder;
	}
	
	
	// Direct binding
	@Bean
	public Binding directBinding1(Queue QueueCancelledOrders, DirectExchange directExchange) {
		return BindingBuilder.bind(QueueCancelledOrders).to(directExchange).with(keyOrderCancel);
	}

	@Bean
	public Binding directBinding2(Queue QueueProcessedOrders, DirectExchange directExchange) {
		return BindingBuilder.bind(QueueProcessedOrders).to(directExchange).with(keyOrderProcess);
	}
	
	@Bean
	public Binding directBinding3(Queue QueueCompletedOrders, DirectExchange directExchange) {
		return BindingBuilder.bind(QueueCompletedOrders).to(directExchange).with(keyOrderComplete);
	}
	
	//Topic binding
	@Bean
	public Binding topicBinding1(Queue QueueProcessedOrders, TopicExchange exchange) {
		return BindingBuilder.bind(QueueProcessedOrders).to(exchange).with(keyOrder);
	}
	@Bean
	public Binding topicBinding2(Queue QueueCompletedOrders, TopicExchange exchange) {
		return BindingBuilder.bind(QueueCompletedOrders).to(exchange).with(keyOrder);
	}
	@Bean
	public Binding topicBinding3(Queue QueueCancelledOrders, TopicExchange exchange) {
		return BindingBuilder.bind(QueueCancelledOrders).to(exchange).with(previousKeyOrder);
	}
	
	//fanout binding
	@Bean
	public Binding fanoutBinding1(Queue QueueCancelledOrders, FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(QueueCancelledOrders).to(fanoutExchange);
	}

	@Bean
	public Binding fanoutBinding2(Queue QueueProcessedOrders, FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(QueueProcessedOrders).to(fanoutExchange);
	}
	
	@Bean
	public Binding fanoutBinding3(Queue QueueCompletedOrders, FanoutExchange fanoutExchange) {
		return BindingBuilder.bind(QueueCompletedOrders).to(fanoutExchange);
	}
	//header binding
	@Bean
	public Binding binding() {
		Map<String, Object> headers = new HashMap<>();
		headers.put("type", "order");
		headers.put("action", "create");

		return BindingBuilder.bind(QueueCompletedOrders())
				.to(headersExchange())
				.whereAny(headers)
				.match();
	}

	@Bean
	public AmqpTemplate template(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		return rabbitTemplate;
	}
}
