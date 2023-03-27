package com.hazelsoft.springsecurityjpa.config;

import com.hazelsoft.springsecurityjpa.entity.Audit;
import com.hazelsoft.springsecurityjpa.entity.Role;
import com.hazelsoft.springsecurityjpa.entity.User;
import com.hazelsoft.springsecurityjpa.model.RequestResponse;
import com.hazelsoft.springsecurityjpa.rabbitmq.Publisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {


    @Bean
    public User getUserBean() {
        return new User();
    }
    @Bean
    public Role getRoleBean() {
        return new Role();
    }
    @Bean
    public Audit getAuditBean() {
        return new Audit();
    }

    @Bean
    public RequestResponse getRequestResponseBean() {
        return new RequestResponse();
    }


}
