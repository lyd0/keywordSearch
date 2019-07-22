package com.advanceSearch.controller;

import com.advanceSearch.dao.AdvanceSearchDao;
import com.advanceSearch.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class PageController {

    @Autowired
    UserDao userDao;
    @GetMapping("/login")
    public String getLoginPage(HttpServletRequest request){
        return "login";
    }
    @PostMapping("/login")
    public String  vertifyLogin(HttpServletRequest request, HttpServletResponse response){
        String password = request.getParameter("password");
        if(userDao.getAdminPassword().equals(password)) {
            request.getSession().setAttribute("isLogin", "true");
        }
        return "redirect:/index";
    }

}
