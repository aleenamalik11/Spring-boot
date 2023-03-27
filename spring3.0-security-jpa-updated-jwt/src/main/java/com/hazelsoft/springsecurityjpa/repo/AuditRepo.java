package com.hazelsoft.springsecurityjpa.repo;

import com.hazelsoft.springsecurityjpa.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepo extends JpaRepository<Audit, Long> {

}
