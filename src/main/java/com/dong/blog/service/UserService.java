package com.dong.blog.service;

import com.dong.blog.pojo.User;

public interface UserService {

    User checkUser(String username, String password);

    User saveUser(User user);
}
