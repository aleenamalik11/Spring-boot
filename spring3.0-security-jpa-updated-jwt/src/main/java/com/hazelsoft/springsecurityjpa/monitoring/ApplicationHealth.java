package com.hazelsoft.springsecurityjpa.monitoring;

import com.hazelsoft.springsecurityjpa.exception.CustomException;
import com.hazelsoft.springsecurityjpa.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import javax.sql.DataSource;
import java.sql.SQLException;


public class ApplicationHealth implements HealthIndicator {

    private static final Logger logger= LoggerFactory.getLogger(ApplicationHealth.class);
    private DataSource datasource;

    public ApplicationHealth(DataSource datasource){
        this.datasource=datasource;
    }
    @Override
    public Health getHealth(boolean includeDetails) {
        return HealthIndicator.super.getHealth(includeDetails);
    }

    @Override
    public Health health() {
        try {
            if (datasource.getConnection().isValid(1000)) {
                return Health.up().withDetail(AuthService.class.toString(), "is running as data ")
                        .build();
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new CustomException(e.getMessage());
        }
        return Health.down().withDetail(AuthService.class.toString(), "is down").build();
    }
}
