package com.hazelsoft.springsecurityjpa.service;

import java.util.ArrayList;
import java.util.List;

import com.hazelsoft.springsecurityjpa.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hazelsoft.springsecurityjpa.entity.User;
import com.hazelsoft.springsecurityjpa.repo.UserRepo;

@Service
public class UserService
{

    private UserRepo userRepository;
    public UserService(UserRepo userRepository){
        this.userRepository=userRepository;
    }

    //getting all User records
    public Page<User> getAllUser(Pageable pageable) {
        return  userRepository.findAll(pageable);
    }
    //getting a specific record
    public User getUserById(Integer id)
    {
        return userRepository.findById(id).get();
    }
    public User getUserByUserName(String username)
    {
        return userRepository.findByUserName(username);
    }
    public User saveOrUpdate(User User)
    {
        return userRepository.save(User);
    }
    //deleting a specific record
    public void delete(Integer id)
    {
        userRepository.deleteById(id);
    }

    public List<Role> getUserRoles(Integer userId){
        return userRepository.getUserRoles(userId);
    }
}
