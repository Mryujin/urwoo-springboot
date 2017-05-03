package com.urwoo.service.impl;

import com.urwoo.dao.RoleDao;
import com.urwoo.dao.UserDao;
import com.urwoo.dao.UserRoleDao;
import com.urwoo.exception.UserNotExitException;
import com.urwoo.exception.UserUsernameExitException;
import com.urwoo.framework.lang.StringUtils;
import com.urwoo.framework.utils.Assert;
import com.urwoo.po.RolePo;
import com.urwoo.po.UserPo;
import com.urwoo.po.UserRolePo;
import com.urwoo.query.UserQueryParam;
import com.urwoo.service.UserService;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private RoleDao roleDao;

    public int saveUser(UserPo userPo) throws UserUsernameExitException {

        Assert.notNull(userPo.getName(), "user name is null!");
        Assert.notNull(userPo.getUsername(), "user username is null!");

        if (checkUserUsernameIsExit(userPo.getUsername())){
            throw new UserUsernameExitException();
        }
        userPo.setPassword("123");
        return userDao.save(userPo);
    }

    public void updateUser(UserPo userPo) throws UserNotExitException, UserUsernameExitException {

        Assert.notNull(userPo.getId(), "user id is null!");
        Assert.notNull(userPo.getName(), "user name is null!");
        Assert.notNull(userPo.getUsername(), "user username is null!");

        UserPo _userPo = getUser(userPo.getId());
        if (null == _userPo){
            throw new UserNotExitException();
        }
        if (!userPo.getUsername().equals(_userPo.getUsername())){
            if (checkUserUsernameIsExit(userPo.getUsername())){
                throw new UserUsernameExitException();
            }
        }
        userPo.setPassword(_userPo.getPassword());
        userDao.update(userPo);
    }

    public void deleteUser(Integer... ids) {
        userDao.delete(ids);
    }

    public void userGrantRole(Integer userId, Integer[] roleIds) {
        Assert.notNull(userId, "userId is null!");
        Assert.notEmpty(roleIds, "roleIds is empty!");

        //获取用户目前的所有角色
        List<RolePo> rolePos = roleDao.userRoleList(userId);
        Set<Integer> roleSets = new HashSet<Integer>();
        if (null != rolePos && !rolePos.isEmpty()) {
            for (RolePo rolePo : rolePos) {
                roleSets.add(rolePo.getId());
                if (!com.urwoo.framework.lang.Arrays.contains(roleIds, rolePo.getId())) {
                    userRoleDao.delete(userId, rolePo.getId());
                }
            }
        }
        UserRolePo userRolePo = null;
        for (Integer roleId : roleIds){
            // 选择的不在原来的角色列表中
            if (!roleSets.contains(roleId)){
                userRolePo = new UserRolePo();
                userRolePo.setRoleId(roleId);
                userRolePo.setUserId(userId);
                userRoleDao.save(userRolePo);
            }
        }
    }

    public UserPo getUser(Integer id) {
        return userDao.get(id);
    }

    public UserPo getUser(String username) {
        return userDao.get(username);
    }

    @Override
    public UserPo login(String username, String password) {
        UserPo userPo = getUser(username);
        if (null == userPo)
            throw new UnknownAccountException("用户名输入错误！");
        if(!userPo.getPassword().equals(password))
            throw new IncorrectCredentialsException("密码输入错误");
        return userPo;
    }

    public List<UserPo> userList(UserQueryParam queryParam, int start, int pageSize) {
        return userDao.list(transformUserQueryParam(queryParam), start, pageSize);
    }

    public int userCount(UserQueryParam queryParam) {
        return userDao.count(transformUserQueryParam(queryParam));
    }

    private Map<String, Object> transformUserQueryParam(UserQueryParam queryParam){
        Map<String, Object> param = new HashMap<String,Object>();

        if (!StringUtils.isBlank(queryParam.getName())){
            param.put("name", queryParam.getName());
        }
        return param;
    }

    private boolean checkUserUsernameIsExit(String username){
        return null != getUser(username);
    }
}
