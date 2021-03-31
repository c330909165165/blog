package com.dong.blog.service;


import com.dong.blog.NotFoundException;
import com.dong.blog.dao.UserRepository;
import com.dong.blog.pojo.User;
import com.dong.blog.util.MD5Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PersonalImpl implements PersonalService{
    @Autowired
    private UserRepository userRepository;


    @Transactional
    @Override
    public String getNickname(Long id) {
        Optional<User> opt = userRepository.findById(id);
        if(!opt.isPresent()){
            throw new NotFoundException("找不到该用户昵称");
        }
        if("".equals(opt.get().getNickname())){
            return "   ";
        }
        return opt.get().getNickname();
    }

    @Transactional
    @Override
    public String getUsername(Long id) {
        Optional<User> opt = userRepository.findById(id);
        if(!opt.isPresent()){
            throw new NotFoundException("找不到该用户名");
        }
        if("".equals(opt.get().getNickname())){
            return "   ";
        }
        return opt.get().getUsername();
    }

    @Transactional
    @Override
    public String getEmail(Long id) {
        Optional<User> opt = userRepository.findById(id);
        if(!opt.isPresent()){
            throw new NotFoundException("找不到该邮箱");
        }
        if("".equals(opt.get().getNickname())){
            return "   ";
        }
        return opt.get().getEmail();
    }


    @Transactional
    @Override
    public String getAvatar(Long id) {
        Optional<User> opt = userRepository.findById(id);
        if(!opt.isPresent()){
            throw new NotFoundException("找不到该图片");
        }
        if("".equals(opt.get().getNickname())){
            return "   ";
        }
        return opt.get().getAvatar();
    }

    @Transactional
    @Override
    public User updatePassword(Long id, String password) {
        Optional<User> opt = userRepository.findById(id);
        opt.get().setPassword(MD5Util.code(password));
        return opt.get();
    }

    @Transactional
    @Override
    public User updateNickname(Long id, String nickname) {
        Optional<User> opt = userRepository.findById(id);
        if(!opt.isPresent()){
            throw new NotFoundException("不存在该用户");
        }
        BeanUtils.copyProperties(nickname,opt.get().getNickname());
        return opt.get();
    }

    @Transactional
    @Override
    public User updateAvatar(Long id, String avatar) {
        Optional<User> opt = userRepository.findById(id);
        BeanUtils.copyProperties(avatar,opt.get().getAvatar());
        return opt.get();
    }

    @Transactional
    @Override
    public User updateEmail(Long id, String email) {
        Optional<User> opt = userRepository.findById(id);
        BeanUtils.copyProperties(email,opt.get().getEmail());
        return opt.get();
    }
}
