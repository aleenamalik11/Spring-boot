package com.hazelsoft.springsecurityjpa.repo;

import com.hazelsoft.springsecurityjpa.entity.Audit;
import com.hazelsoft.springsecurityjpa.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepo extends JpaRepository<Audit, Long> {

}
