package com.dong.blog.web.user;

import com.dong.blog.dao.UserRepository;
import com.dong.blog.pojo.User;
import com.dong.blog.service.PersonalService;
import com.dong.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class PersonalController {

    @Autowired
    private PersonalService personalService;

    @GetMapping("/personal")
    public String user(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        user.setBlogs(null);
        model.addAttribute("user",user);
        model.addAttribute("nickname",user.getNickname());
        model.addAttribute("email",user.getEmail());
        model.addAttribute("avatar",user.getAvatar());
        System.out.println(model);
        return "user/personal";
    }

}
