package com.hazelsoft.springsecurityjpa.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public abstract class Consumer<T, W>{

    @RabbitListener(queues = "${audit.queue}")
    protected void receiver(T message) {
        W resp = processReceivedMsg(message);
        System.out.println(resp);
    }

    protected abstract W processReceivedMsg(T message);

}
