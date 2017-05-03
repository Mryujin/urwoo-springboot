package com.urwoo.web.controller;

import com.urwoo.framework.lang.StringUtils;
import com.urwoo.framework.web.controller.base.BaseController;
import com.urwoo.framework.web.response.UrwooResponse;
import com.urwoo.framework.web.response.UrwooResponses;
import com.urwoo.po.RolePo;
import com.urwoo.query.RoleQueryParam;
import com.urwoo.query.order.OrderParam;
import com.urwoo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role/")
public class RoleController extends BaseController{

    @Autowired
    private RoleService roleService;

    @GetMapping(path = "query", produces = {DEFAULT_PRODUCES})
    public UrwooResponse query(@RequestParam(name = "name", required = false) String name,
                               @RequestParam(name = "propertyName", required = false) String propertyName,
                               @RequestParam(name = "isAsc", required = false)Boolean isAsc,
                               @RequestParam(name = "start", defaultValue = DEFAULT_START) Integer start,
                               @RequestParam(name = "limit", defaultValue = DEFAULT_LIMIT) Integer limit){

        RoleQueryParam param = new RoleQueryParam();

        if (StringUtils.isNotBlank(name)) {
            param.setName(name);
        }
        if (StringUtils.isNotBlank(propertyName)){
            OrderParam orderParam = new OrderParam();
            orderParam.setPropertyName(propertyName);
            orderParam.setAsc(null == isAsc ? true : isAsc);
            param.setOrderParam(orderParam);
        }

        List<RolePo> roleList = roleService.roleList(param, start, limit);
        int count = roleService.roleCount(param);
        return UrwooResponses.page(roleList, count, start, limit);
    }

    @GetMapping(path = "get/{id}", produces = {DEFAULT_PRODUCES})
    public UrwooResponse get(@PathVariable(name = "id") Integer id){
        RolePo rolePo = roleService.getRole(id);
        return UrwooResponses.success(rolePo);
    }

    @PostMapping(path = "save", produces = {DEFAULT_PRODUCES})
    public UrwooResponse save(@RequestParam(name = "name") String name,
                              @RequestParam(name = "roleCode") String roleCode){

        RolePo rolePo = new RolePo();
        rolePo.setName(name);
        rolePo.setRoleCode(roleCode);
        roleService.saveRole(rolePo);
        return UrwooResponses.success();
    }

    @PostMapping(path = "update/{id}", produces = {DEFAULT_PRODUCES})
    public UrwooResponse update(@PathVariable(name = "id") Integer id,
                                @RequestParam(name = "name") String name,
                                @RequestParam(name = "roleCode") String roleCode){

        RolePo rolePo = new RolePo();
        rolePo.setId(id);
        rolePo.setName(name);
        rolePo.setRoleCode(roleCode);
        roleService.updateRole(rolePo);
        return UrwooResponses.success();
    }

    @GetMapping(path = "delete", produces = {DEFAULT_PRODUCES})
    public UrwooResponse delete(@RequestParam(name = "ids") Integer[] ids){
        roleService.deleteRole(ids);
        return UrwooResponses.success();
    }

    @GetMapping(path = "assignRight", produces = {DEFAULT_PRODUCES})
    public UrwooResponse assignRights(@RequestParam(name = "roleId") Integer roleId,
                                      @RequestParam(name = "rightIds") Integer[] rightIds){

        roleService.roleGrantRight(roleId, rightIds);
        return UrwooResponses.success();
    }
}