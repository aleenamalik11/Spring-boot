package com.hazelsoft.springsecurityjpa.config;

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
	@Value("${audit.queue}")
	private String queueAudit;

	@Value("${direct.exchange}")
	private String directAuditExchange;

	@Value("${key.audit}")
	private String key;

	@Bean
	public String key(){
		return key;
	}

	// Creating  queues
	@Bean
	public Queue queueAudit() {
		return new Queue(queueAudit);
	}
	
	//creating exchanges
	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange(directAuditExchange);
	}

	//fanout binding
	@Bean
	public Binding directBinding(Queue queueAudit, DirectExchange directExchange) {
		return BindingBuilder.bind(queueAudit).to(directExchange).with(key);
	}

	@Bean
	public AmqpTemplate template(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		return rabbitTemplate;
	}
}
