package com.urwoo.dao;

import com.urwoo.framework.jdbc.core.CommonDao;
import com.urwoo.po.UserPo;

public interface UserDao extends CommonDao<UserPo> {

    UserPo get(String username);
}
