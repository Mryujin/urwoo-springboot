package com.urwoo;

import com.urwoo.mongodb.Application;
import com.urwoo.mongodb.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class AppTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testInsert(){
        userRepository.count("aaaa", null);
    }
}
