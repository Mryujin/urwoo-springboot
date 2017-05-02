package com.urwoo.dao;

import com.urwoo.po.UserRolePo;

public interface UserRoleDao {

    void save(UserRolePo userRolePo);

    void delete(Integer userId, Integer roleId);
}
