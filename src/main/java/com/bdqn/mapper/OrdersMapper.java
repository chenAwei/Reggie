package com.bdqn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bdqn.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
