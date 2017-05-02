package com.urwoo.query;

import com.urwoo.query.order.OrderParam;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RightQueryParam {

    private String name;
    private OrderParam orderParam;
}
