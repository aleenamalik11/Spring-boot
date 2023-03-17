package com.hazelsoft.springsecurityjpa.service;

import com.hazelsoft.springsecurityjpa.entity.Role;
import com.hazelsoft.springsecurityjpa.repo.RoleRepo;
import com.hazelsoft.springsecurityjpa.repo.UserRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private RoleRepo roleRepository;
    public RoleService(RoleRepo roleRepository){
        this.roleRepository=roleRepository;
    }

    public String getAdminRole(){
        Role role = roleRepository.findByName("ROLE_ADMIN");
        return role.getName();
    }

    public String getUserRole(){
        Role role = roleRepository.findByName("ROLE_USER");
        return role.getName();
    }

    public String getEditorRole(){
        Role role = roleRepository.findByName("ROLE_EDITOR");
        return role.getName();
    }

    public List<String> getAllRoles(){
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map((Role role) -> new String(role.getName())).collect(Collectors.toList());
    }
}
