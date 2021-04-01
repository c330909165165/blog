package com.dong.blog.web;

import com.dong.blog.pojo.User;
import com.dong.blog.service.UserService;
import com.dong.blog.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.sql.Date;
import java.time.Instant;

@Controller
@RequestMapping("sign")
public class SignController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String sign(){
        return "sign";
    }

    @PostMapping("/in")
    public String signIn(@Valid User user,RedirectAttributes attributes){
        user.setCreateTime(Date.from(Instant.now()));
        user.setUpdateTime(Date.from(Instant.now()));
        user.setType(2);
        user.setAvatar("https://picsum.photos/id/1050/100/100");
        user.setPassword(MD5Util.code(user.getPassword()));
        userService.saveUser(user);
        attributes.addFlashAttribute("message","注册成功");
        return "redirect:/login";
    }
}
