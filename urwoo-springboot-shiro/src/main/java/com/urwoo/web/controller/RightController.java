package com.urwoo.web.controller;

import com.urwoo.framework.lang.StringUtils;
import com.urwoo.framework.web.controller.base.BaseController;
import com.urwoo.framework.web.response.UrwooResponse;
import com.urwoo.framework.web.response.UrwooResponses;
import com.urwoo.po.RightPo;
import com.urwoo.query.RightQueryParam;
import com.urwoo.query.order.OrderParam;
import com.urwoo.service.RightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/right/")
public class RightController extends BaseController{

    @Autowired
    private RightService rightService;

    @GetMapping(path = "query", produces = {DEFAULT_PRODUCES})
    public UrwooResponse query(@RequestParam(name = "name", required = false) String name,
                               @RequestParam(name = "propertyName", required = false) String propertyName,
                               @RequestParam(name = "isAsc", required = false)Boolean isAsc,
                               @RequestParam(name = "start", defaultValue = DEFAULT_START) Integer start,
                               @RequestParam(name = "limit", defaultValue = DEFAULT_LIMIT) Integer limit){

        RightQueryParam param = new RightQueryParam();
        if (StringUtils.isNotBlank(name)) {
            param.setName(name);
        }
        if (StringUtils.isNotBlank(propertyName)){
            OrderParam orderParam = new OrderParam();
            orderParam.setPropertyName(propertyName);
            orderParam.setAsc(null == isAsc ? true : isAsc);
            param.setOrderParam(orderParam);
        }

        List<RightPo> rightList = rightService.rightList(param, start, limit);
        int count = rightService.rightCount(param);
        return UrwooResponses.page(rightList, count, start, limit);
    }

    @GetMapping(path = "get/{id}", produces = {DEFAULT_PRODUCES})
    public UrwooResponse get(@PathVariable(name = "id") Integer id){
        RightPo rightPo = rightService.getRight(id);
        return UrwooResponses.success(rightPo);
    }

    @PostMapping(path = "save", produces = {DEFAULT_PRODUCES})
    public UrwooResponse save(@RequestParam(name = "name") String name,
                              @RequestParam(name = "type") Integer type,
                              @RequestParam(name = "url") String url,
                              @RequestParam(name = "rightCode") String rightCode,
                              @RequestParam(name = "parentId") Integer parentId){

        RightPo rightPo = new RightPo();
        rightPo.setName(name);
        rightPo.setType(type);
        rightPo.setUrl(url);
        rightPo.setRightCode(rightCode);
        rightPo.setParentId(parentId);
        rightService.saveRight(rightPo);
        return UrwooResponses.success();
    }

    @PostMapping(path = "update/{id}", produces = {DEFAULT_PRODUCES})
    public UrwooResponse update(@PathVariable(name = "id") Integer id,
                                @RequestParam(name = "name") String name,
                                @RequestParam(name = "type") Integer type,
                                @RequestParam(name = "url") String url,
                                @RequestParam(name = "rightCode") String rightCode,
                                @RequestParam(name = "parentId") Integer parentId){

        RightPo rightPo = new RightPo();
        rightPo.setId(id);
        rightPo.setName(name);
        rightPo.setType(type);
        rightPo.setUrl(url);
        rightPo.setRightCode(rightCode);
        rightPo.setParentId(parentId);
        rightService.updateRight(rightPo);
        return UrwooResponses.success();
    }

    @GetMapping(path = "delete", produces = {DEFAULT_PRODUCES})
    public UrwooResponse delete(@RequestParam(name = "ids") Integer[] ids){
        rightService.deleteRight(ids);
        return UrwooResponses.success();
    }
}