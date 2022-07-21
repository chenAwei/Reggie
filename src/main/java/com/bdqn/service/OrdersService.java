package com.bdqn.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bdqn.pojo.Orders;

public interface OrdersService extends IService<Orders> {

    void submit(Orders orders);

}
