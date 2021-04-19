package com.dong.blog.service;

import com.dong.blog.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User checkUser(String username, String password);

    User saveUser(User user);

    Page<User> listUser(Pageable pageable);

    void deleteUser(Long id);
}
