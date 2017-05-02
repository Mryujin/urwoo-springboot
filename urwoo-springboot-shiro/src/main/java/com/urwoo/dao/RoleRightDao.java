package com.urwoo.dao;

import com.urwoo.po.RoleRightPo;

public interface RoleRightDao {

    void save(RoleRightPo roleRightPo);

    void delete(Integer roleId, Integer rightId);
}
