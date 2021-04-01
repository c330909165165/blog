package com.dong.blog.web.user;

import com.dong.blog.dao.UserRepository;
import com.dong.blog.pojo.User;
import com.dong.blog.service.PersonalService;
import com.dong.blog.service.UserService;
import com.dong.blog.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class PersonalController {

    @Autowired
    private PersonalService personalService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/personal")
    public String user(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        model.addAttribute("nickname",user.getNickname());
        model.addAttribute("email",user.getEmail());
        model.addAttribute("avatar",user.getAvatar());
        System.out.println(model);
        return "user/personal";
    }
    @GetMapping("/password-input")
    public String passwordInput(){
        return "user/password-input";
    }

    @PostMapping("/password-input")
    public String post(HttpSession session,@RequestBody String password){
        User user = (User) session.getAttribute("user");
        Optional<User> opt = userRepository.findById(user.getId());
      opt.ifPresent(user1 -> {
              user1.setPassword(MD5Util.code(password.substring(5)));
              userRepository.save(user1);
      });
        return "redirect:/user/personal";
    }
    @GetMapping("/nickname-input")
    public String nickname(){
        return "user/nickname-input";
    }

    @PostMapping("/nickname-input")
    public String nicknameInput(HttpSession session,@RequestBody String nickname){
        User user = (User) session.getAttribute("user");
        Optional<User> opt = userRepository.findById(user.getId());
        opt.ifPresent(user1 -> {
            user1.setNickname(nickname.substring(5));
            userRepository.save(user1);
            user1.setPassword(null);
            session.removeAttribute("user");
            session.setAttribute("user",user1);
        });
        return "redirect:/user/personal";
    }
    @GetMapping("/email-input")
    public String email(){
        return "user/email-input";
    }
    @PostMapping("/email-input")
    public String emailInput(HttpSession session,@RequestBody String email){
        User user = (User) session.getAttribute("user");
        Optional<User> opt = userRepository.findById(user.getId());

        System.out.println(email);
        opt.ifPresent(user1 -> {
            user1.setEmail(email.replace("%40","@").substring(5));
            userRepository.save(user1);
            user1.setPassword(null);
            session.removeAttribute("user");
            session.setAttribute("user",user1);
        });
        return "redirect:/user/personal";
    }


}
