package com.dong.blog.service;

import com.dong.blog.dao.BlogRepository;
import com.dong.blog.dao.UserRepository;
import com.dong.blog.pojo.User;
import com.dong.blog.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, MD5Util.code(password));
        return user;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Page<User> listUser(Pageable pageable) {
        List<User> users = userRepository.findAll();
        List<User> users1 = new ArrayList<>();
        for(User user : users){
            if(user.getType() == 2){
                users1.add(user);
            }
        }
        Page<User> page = new PageImpl<User>(users1,pageable,users1.size());
        return page;
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        Optional<User> byId = userRepository.findById(id);
        byId.ifPresent(user -> {
            blogRepository.deleteAllByUser(user);
            userRepository.deleteById(id);
        });
    }
}
