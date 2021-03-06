package com.urwoo.web.controller;

import com.urwoo.exception.UserNotExitException;
import com.urwoo.exception.UserUsernameExitException;
import com.urwoo.framework.lang.StringUtils;
import com.urwoo.framework.web.controller.base.BaseController;
import com.urwoo.framework.web.response.UrwooResponse;
import com.urwoo.framework.web.response.UrwooResponses;
import com.urwoo.po.UserPo;
import com.urwoo.query.UserQueryParam;
import com.urwoo.query.order.OrderParam;
import com.urwoo.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @GetMapping(path = "query", produces = {DEFAULT_PRODUCES})
    @RequiresPermissions(value = "user:query")
    public UrwooResponse query(@RequestParam(name = "name", required = false) String name,
                               @RequestParam(name = "propertyName", required = false) String propertyName,
                               @RequestParam(name = "isAsc", required = false)Boolean isAsc,
                               @RequestParam(name = "start", defaultValue = DEFAULT_START) Integer start,
                               @RequestParam(name = "limit", defaultValue = DEFAULT_LIMIT) Integer limit){

        UserQueryParam param = new UserQueryParam();
        if (StringUtils.isNotBlank(name)){
            param.setName(name);
        }
        if (StringUtils.isNotBlank(propertyName)){
            OrderParam orderParam = new OrderParam();
            orderParam.setPropertyName(propertyName);
            orderParam.setAsc(null == isAsc ? true : isAsc);
            param.setOrderParam(orderParam);
        }
        List<UserPo> userList = userService.userList(param, start, limit);
        int count = userService.userCount(param);
        return UrwooResponses.page(userList, count, start, limit);
    }

    @GetMapping(path = "get/{id}", produces = {DEFAULT_PRODUCES})
    public UrwooResponse get(@PathVariable(name = "id") Integer id){
        UserPo userPo = userService.getUser(id);
        return UrwooResponses.success(userPo);
    }

    @PostMapping(path = "save", produces = {DEFAULT_PRODUCES})
    public UrwooResponse save(@RequestParam(name = "name") String name,
                              @RequestParam(name = "username") String username){

        UserPo userPo = new UserPo();
        userPo.setName(name);
        userPo.setUsername(username);
        try {
            userService.saveUser(userPo);
        } catch (UserUsernameExitException e) {
            return UrwooResponses.fail("用户名已经存在！");
        }
        return UrwooResponses.success();
    }

    @PostMapping(path = "update/{id}", produces = {DEFAULT_PRODUCES})
    public UrwooResponse update(@PathVariable(name = "id") Integer id,
                                @RequestParam(name = "name") String name,
                                @RequestParam(name = "username") String username){

        UserPo userPo = new UserPo();
        userPo.setId(id);
        userPo.setName(name);
        userPo.setUsername(username);

        try {
            userService.updateUser(userPo);
        } catch (UserNotExitException e) {
            return UrwooResponses.fail("用户对象不存在！");
        } catch (UserUsernameExitException e) {
            return UrwooResponses.fail("用户名已经存在！");
        }
        return UrwooResponses.success();
    }

    @GetMapping(path = "delete", produces = {DEFAULT_PRODUCES})
    public UrwooResponse delete(@RequestParam(name = "ids") Integer[] ids){
        userService.deleteUser(ids);
        return UrwooResponses.success();
    }

    @PostMapping(path = "assignRole", produces = {DEFAULT_PRODUCES})
    public UrwooResponse assignRoles(@RequestParam(name = "userId") Integer userId,
                                     @RequestParam(name = "roleIds") Integer[] roleIds){

        userService.userGrantRole(userId, roleIds);
        return UrwooResponses.success();
    }
}