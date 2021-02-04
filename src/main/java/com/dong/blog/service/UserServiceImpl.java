package com.dong.blog.service;

import com.dong.blog.dao.UserRepository;
import com.dong.blog.pojo.User;
import com.dong.blog.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, MD5Util.code(password));
        return user;
    }

    @Override
    public User saveUser(User user) {

        return userRepository.save(user);
    }
}
