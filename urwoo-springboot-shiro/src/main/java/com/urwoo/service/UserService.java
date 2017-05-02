package com.urwoo.service;

import com.urwoo.exception.UserNotExitException;
import com.urwoo.exception.UserUsernameExitException;
import com.urwoo.po.UserPo;
import com.urwoo.query.UserQueryParam;

import java.util.List;

public interface UserService {

    int saveUser(UserPo rolePo) throws UserUsernameExitException;

    void updateUser(UserPo rolePo) throws UserNotExitException, UserUsernameExitException;

    void deleteUser(Integer... ids);

    void userGrantRole(Integer userId, Integer[] roleIds);

    UserPo getUser(Integer id);

    UserPo getUser(String username);

    UserPo login(String username, String password);

    List<UserPo> userList(UserQueryParam queryParam, int start, int pageSize);

    int userCount(UserQueryParam queryParam);
}
