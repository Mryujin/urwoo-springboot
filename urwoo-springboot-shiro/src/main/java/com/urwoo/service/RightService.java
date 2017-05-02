package com.urwoo.service;


import com.urwoo.po.RightPo;
import com.urwoo.query.RightQueryParam;

import java.util.List;

public interface RightService {

    int saveRight(RightPo rightPo);

    void updateRight(RightPo rightPo);

    void deleteRight(Integer... ids);

    RightPo getRight(Integer id);

    List<RightPo> rightList(RightQueryParam queryParam, int start, int pageSize);

    List<RightPo> userRightList(Integer userId);

    int rightCount(RightQueryParam queryParam);
}
