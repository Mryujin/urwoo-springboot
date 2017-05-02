package com.urwoo.dao;

import com.urwoo.framework.jdbc.core.CommonDao;
import com.urwoo.po.RolePo;

import java.util.List;

public interface RoleDao extends CommonDao<RolePo> {

    List<RolePo> userRoleList(Integer userId);

    RolePo get(String name);
}
