package com.urwoo.dao;

import com.urwoo.framework.jdbc.core.CommonDao;
import com.urwoo.po.RightPo;

import java.util.List;

public interface RightDao extends CommonDao<RightPo>{

    List<RightPo> roleRightList(Integer roleId);

    List<RightPo> userRightList(Integer userId);
}
