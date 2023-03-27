package com.hazelsoft.springsecurityjpa.service;

import java.util.List;

import com.hazelsoft.springsecurityjpa.entity.Role;
import com.hazelsoft.springsecurityjpa.rabbitmq.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hazelsoft.springsecurityjpa.entity.User;
import com.hazelsoft.springsecurityjpa.repo.UserRepo;

@Service
public class UserService
{

    private UserRepo userRepository;

    private Publisher publisher;
    public UserService(UserRepo userRepository, Publisher publisher){
        this.userRepository=userRepository;
        this.publisher = publisher;
    }

    //getting all User records
    public Page<User> getAllUser(Pageable pageable) {
        Page<User> userPage =  userRepository.findAll(pageable);
        if(userPage != null){
            publisher.saveActivityAudit("User Page accessed successfully");
        }
        return userPage;
    }
    //getting a specific record
    public User getUserById(Integer id) throws Exception
    {
        User user = userRepository.findById(id).get();
        if(user != null){
            publisher.saveActivityAudit("User fetched successfully  with user name "
                    + user.getUserName());
        }
        return user;
    }
    public User getUserByUserName(String username)
    {
        User user = userRepository.findByUserName(username);
        if(user != null){
            publisher.saveActivityAudit("User fetched successfully  with user name "
                    + user.getUserName());
        }
        return user;
    }
    public User saveOrUpdate(User User)
    {
        User savedUser = userRepository.save(User);
        if(savedUser != null){
            publisher.saveActivityAudit("User saved successfully  with user name "
                    + savedUser.getUserName());
        }
        return savedUser;
    }
    //deleting a specific record
    public void delete(Integer id)
    {
        userRepository.deleteById(id);
        publisher.saveActivityAudit("User deleted successfully  with user id " + id);
    }

    public List<Role> getUserRoles(Integer userId){

        List<Role> roles = userRepository.getUserRoles(userId);
        if(roles != null){
            publisher.saveActivityAudit("User Roles fetched successfully  " +
                    "with user id " + userId);
        }
        return roles;
    }
}
