package com.sheng.example;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Administrator on 2017/6/30 0030.
 */
@Controller
public class MyController {

    @RequestMapping("test")
    @ResponseBody
    public String index(){
        Subject currentUser = SecurityUtils.getSubject();
        return "Hello:"+currentUser.getPrincipal().toString()+",我是tomcat2";
    }
    @RequestMapping("login")
    public ModelAndView login(String username,String password){
        ModelAndView modelAndView=null;
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            modelAndView=new ModelAndView("test");
            modelAndView.addObject("username",username);
        }catch (AuthenticationException ae){
            modelAndView=new ModelAndView("/");
            modelAndView.addObject("message","用户名或密码错误");
        }

        return modelAndView;
    }
}
