package com.hazelsoft.springsecurityjpa.rabbitmq;

import com.hazelsoft.springsecurityjpa.entity.Audit;
import com.hazelsoft.springsecurityjpa.service.AuditService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AuditConsumerImpl extends Consumer<String, Audit>{
    private AuditService auditService;
    public AuditConsumerImpl( AuditService auditService){
        this.auditService = auditService;
    }
    @Override
    protected Audit processReceivedMsg(String message) {
        Audit auditInfo = new Audit();

        auditInfo.setDetails(message);
        auditInfo.setLastUpdatedAt(new Date());
        return auditService.save(auditInfo);
    }
}
