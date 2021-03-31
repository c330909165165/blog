package com.dong.blog.service;

import com.dong.blog.pojo.User;
import org.springframework.stereotype.Service;

@Service
public interface PersonalService {

    String getNickname(User user);

    String getUsername(User user);

    String getEmail(User user);

    String getAvatar(User user);

    User updateNickname(Long id,User user);

    User updateAvatar(Long id,User user);
}
