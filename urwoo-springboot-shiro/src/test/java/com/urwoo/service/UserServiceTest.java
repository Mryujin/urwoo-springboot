package com.urwoo.service;

import com.urwoo.Application;
import com.urwoo.exception.UserUsernameExitException;
import com.urwoo.po.UserPo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void saveUserTest(){
        UserPo userPo = new UserPo();
        userPo.setUsername("lisi");
        userPo.setName("ls");
        userPo.setPassword("123");

        try {
            userService.saveUser(userPo);
        } catch (UserUsernameExitException e) {
            e.printStackTrace();
        }
    }
}
