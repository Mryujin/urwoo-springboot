package com.urwoo.service.impl;

import com.urwoo.dao.RightDao;
import com.urwoo.dao.RoleDao;
import com.urwoo.dao.RoleRightDao;
import com.urwoo.framework.lang.StringUtils;
import com.urwoo.framework.utils.Assert;
import com.urwoo.po.RightPo;
import com.urwoo.po.RolePo;
import com.urwoo.po.RoleRightPo;
import com.urwoo.query.RoleQueryParam;
import com.urwoo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    protected RoleRightDao roleRightDao;
    @Autowired
    private RightDao rightDao;

    public int saveRole(RolePo rolePo) {
        return roleDao.save(rolePo);
    }

    public void updateRole(RolePo rolePo) {
        roleDao.update(rolePo);
    }

    public void deleteRole(Integer... ids) {
        roleDao.delete(ids);
    }

    public void roleGrantRight(Integer roleId, Integer[] rightIds) {

        Assert.notNull(roleId, "roleId is null!");
        Assert.notEmpty(rightIds, "rightIds is empty!");

        //获取角色目前的所有权限
        List<RightPo> rightPos = rightDao.roleRightList(roleId);

        Set<Integer> rightSets = new HashSet<Integer>();
        if (null != rightPos && !rightPos.isEmpty()) {
            for (RightPo rightPo : rightPos) {
                rightSets.add(rightPo.getId());
                if (!com.urwoo.framework.lang.Arrays.contains(rightIds, rightPo.getId())) {
                    roleRightDao.delete(roleId, rightPo.getId());
                }
            }
        }
        RoleRightPo roleRightPo = null;
        for (Integer rightId : rightIds){
            // 选择的不在原来的角色列表中
            if (!rightSets.contains(rightId)){
                roleRightPo = new RoleRightPo();
                roleRightPo.setRoleId(roleId);
                roleRightPo.setRightId(rightId);
                roleRightDao.save(roleRightPo);
            }
        }
    }

    public RolePo getRole(Integer id) {
        return roleDao.get(id);
    }

    public RolePo getRole(String name) {
        return roleDao.get(name);
    }

    public List<RolePo> userRoleList(Integer userId) {
        return roleDao.userRoleList(userId);
    }

    public List<RolePo> roleList(RoleQueryParam queryParam, int start, int pageSize) {
        return roleDao.list(transformRoleQueryParam(queryParam), start, pageSize);
    }

    public int roleCount(RoleQueryParam queryParam) {
        return roleDao.count(transformRoleQueryParam(queryParam));
    }

    private Map<String, Object> transformRoleQueryParam(RoleQueryParam queryParam){
        Map<String, Object> param = new HashMap<String,Object>();

        if (!StringUtils.isBlank(queryParam.getName())){
            param.put("name", queryParam.getName());
        }
        return param;
    }
}
