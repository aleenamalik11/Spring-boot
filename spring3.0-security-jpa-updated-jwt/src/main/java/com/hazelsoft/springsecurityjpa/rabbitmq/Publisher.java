package com.hazelsoft.springsecurityjpa.rabbitmq;

import com.hazelsoft.springsecurityjpa.model.RequestResponse;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class Publisher<T> {

    private RabbitTemplate rabbitTemplate;

    private DirectExchange directExchange;

    private RequestResponse requestResponse;

    private String key;

    public Publisher(RabbitTemplate rabbitTemplate, DirectExchange directExchange,
                     RequestResponse requestResponse, String key){
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
        this.requestResponse = requestResponse;
        this.key = key;
    }


    public void saveActivityAudit(T details) {
        rabbitTemplate.convertAndSend(directExchange.getName(), key , details);
    }
}
