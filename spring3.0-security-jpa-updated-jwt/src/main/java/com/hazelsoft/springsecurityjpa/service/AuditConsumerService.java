package com.hazelsoft.springsecurityjpa.service;

import com.hazelsoft.springsecurityjpa.entity.Audit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuditConsumerService {
    private AuditService auditService;
    public AuditConsumerService( AuditService auditService){
        this.auditService = auditService;
    }
    @RabbitListener(queues = "${audit.queue}")
    public void receiver(String message) {
        Audit savedAudit = saveAuditInfo(message);
        System.out.println("Audit saved with audit info: " + savedAudit);
    }

    public Audit saveAuditInfo(String auditMsg){
        Audit auditInfo = new Audit();

        auditInfo.setDetails(auditMsg);
        auditInfo.setLastUpdatedAt(new Date());
        return auditService.save(auditInfo);
    }
}
