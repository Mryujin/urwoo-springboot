package com.urwoo.service.impl;

import com.urwoo.dao.RightDao;
import com.urwoo.framework.lang.StringUtils;
import com.urwoo.po.RightPo;
import com.urwoo.query.RightQueryParam;
import com.urwoo.service.RightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RightServiceImpl implements RightService {

    @Autowired
    private RightDao rightDao;

    public int saveRight(RightPo rightPo) {
        return rightDao.save(rightPo);
    }

    public void updateRight(RightPo rightPo) {
        rightDao.update(rightPo);
    }

    public void deleteRight(Integer... ids) {
        rightDao.delete(ids);
    }

    public RightPo getRight(Integer id) {
        return rightDao.get(id);
    }

    public List<RightPo> rightList(RightQueryParam queryParam, int start, int pageSize) {
        return rightDao.list(transformRightQueryParam(queryParam), start, pageSize);
    }

    public List<RightPo> userRightList(Integer userId){
        return rightDao.userRightList(userId);
    }

    public int rightCount(RightQueryParam queryParam) {
        return rightDao.count(transformRightQueryParam(queryParam));
    }

    private Map<String, Object> transformRightQueryParam(RightQueryParam queryParam){
        Map<String, Object> param = new HashMap<String,Object>();

        if (!StringUtils.isBlank(queryParam.getName())){
            param.put("name", queryParam.getName());
        }
        return param;
    }
}
