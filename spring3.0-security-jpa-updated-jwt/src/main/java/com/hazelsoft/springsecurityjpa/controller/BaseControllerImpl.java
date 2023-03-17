package com.hazelsoft.springsecurityjpa.controller;

import com.hazelsoft.springsecurityjpa.dto.RequestResponse;
import com.hazelsoft.springsecurityjpa.dto.Status;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class BaseControllerImpl implements BaseController{

    private RabbitTemplate rabbitTemplate;

    private DirectExchange directExchange;

    private RequestResponse requestResponse;

    private String key;

    public BaseControllerImpl(RabbitTemplate rabbitTemplate, DirectExchange directExchange,
                              RequestResponse requestResponse, String key){
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
        this.requestResponse = requestResponse;
        this.key = key;
    }

    @Override
    public RequestResponse prepareResponse(Object payload, Object errors, String msg, Status status) {
        requestResponse.setErrors(errors);
        requestResponse.setMessage(msg);
        requestResponse.setPayload(payload);
        requestResponse.setStatus(status);

        return requestResponse;
    }

    @Override
    public void saveActivityAudit(String details) {
        rabbitTemplate.convertAndSend(directExchange.getName(), key , details);
    }
}
