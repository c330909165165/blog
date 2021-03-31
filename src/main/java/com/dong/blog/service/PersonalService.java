package com.dong.blog.service;

import com.dong.blog.pojo.User;
import org.springframework.stereotype.Service;

public interface PersonalService {

    String getNickname(Long id);

    String getUsername(Long id);

    String getEmail(Long id);

    String getAvatar(Long id);

    User updatePassword(Long id,String password);

    User updateNickname(Long id,String nickname);

    User updateAvatar(Long id,String avatar);

    User updateEmail(Long id,String email);
}
