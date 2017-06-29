package com.urwoo.web.controller;

import com.urwoo.framework.lang.StringUtils;
import com.urwoo.framework.web.controller.base.BaseController;
import com.urwoo.framework.web.response.UrwooResponse;
import com.urwoo.framework.web.response.UrwooResponses;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth/")
public class AuthController extends BaseController{

    @PostMapping(path = "/login", produces = {DEFAULT_PRODUCES})
    public UrwooResponse login(@RequestParam(name = "username") String username,
                               @RequestParam(name = "password") String password,
                               @RequestParam(name = "checkCode") String checkCode,
                               HttpSession session){

        if (StringUtils.isBlank(username)){
            return UrwooResponses.fail("用户名不能为空！");
        }
        if (StringUtils.isBlank(password)){
            return UrwooResponses.fail("密码不能为空！");
        }
        if (StringUtils.isBlank(checkCode)){
            return UrwooResponses.fail("验证码不能为空！");
        }
        String checkCodeSession = (String) session.getAttribute(CHECK_CODE);
        if (!checkCode.equals(checkCodeSession)){
            return UrwooResponses.fail("验证码输入错误！");
        }
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
            return UrwooResponses.success("登陆成功！");
        }catch(AuthenticationException e){
            return UrwooResponses.fail(e.getMessage());
        }
    }

    @PostMapping(path = "/logout", produces = {DEFAULT_PRODUCES})
    public UrwooResponse logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return UrwooResponses.success("成功退出系统！");
    }
}
