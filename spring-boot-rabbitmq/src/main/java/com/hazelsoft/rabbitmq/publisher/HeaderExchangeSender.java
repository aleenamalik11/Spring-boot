package com.hazelsoft.rabbitmq.publisher;

import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeaderExchangeSender {
    private final RabbitTemplate rabbitTemplate;

    private final HeadersExchange headersExchange;

    @Autowired
    public HeaderExchangeSender(RabbitTemplate rabbitTemplate, HeadersExchange headersExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.headersExchange = headersExchange;
    }

    @GetMapping("/header")
    public String send() {
        MessageProperties messageProperties = new MessageProperties();
        //messageProperties.setHeader("type", "order");
        //messageProperties.setHeader("action", "create");

        Message message = new Message("Message using Header Exchange".getBytes(), messageProperties);
        rabbitTemplate.send(headersExchange.getName(), "", message);
        return "success";
    }
}
