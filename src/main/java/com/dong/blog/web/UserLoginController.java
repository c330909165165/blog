package com.dong.blog.web;

import com.dong.blog.pojo.User;
import com.dong.blog.service.BlogService;
import com.dong.blog.service.TagService;
import com.dong.blog.service.TypeService;
import com.dong.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class UserLoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private TagService tagService;

    @Autowired
    private TypeService typeService;

    @GetMapping
    public String loginPage(){
        return "login";
    }

    @PostMapping("/user")
    public String userLogin(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            RedirectAttributes attributes,
                            Model model,
                            Pageable pageable){
        User user = userService.checkUser(username, password);
        if(user != null){
            user.setPassword(null);
            session.setAttribute("user",user);
            model.addAttribute("page",blogService.listBlog(pageable));
            model.addAttribute("types",typeService.listTypeTop(6));
            model.addAttribute("tags",tagService.listTagTop(10));
            model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(8));
            return "user/index";
        }else{
            attributes.addFlashAttribute("message","用户名和密码错误");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/index";
    }
}

