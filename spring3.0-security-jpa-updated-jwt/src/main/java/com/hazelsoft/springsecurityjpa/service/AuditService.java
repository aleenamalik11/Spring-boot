package com.hazelsoft.springsecurityjpa.service;

import com.hazelsoft.springsecurityjpa.entity.Audit;
import com.hazelsoft.springsecurityjpa.repo.AuditRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuditService {

    private AuditRepo auditRepo;
    public AuditService(AuditRepo auditRepo){

        this.auditRepo = auditRepo;
    }

    public Audit getAuditById(Long id)
    {
        return auditRepo.findById(id).get();
    }
    public List<Audit> getAllAudits()
    {
        List<Audit> audits = new ArrayList<Audit>();
        audits = auditRepo.findAll();
        return audits;
    }
    public Audit save(Audit audit)
    {
        return auditRepo.save(audit);
    }

    public void delete(Long id)
    {
        auditRepo.deleteById(id);
    }
}
