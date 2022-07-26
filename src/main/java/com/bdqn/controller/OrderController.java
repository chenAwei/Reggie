package com.bdqn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdqn.common.R;
import com.bdqn.pojo.Orders;
import com.bdqn.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String number, String beginTime, String endTime){
        Page<Orders> ordersPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(number),Orders::getNumber,number);
        queryWrapper.between(StringUtils.isNotEmpty(beginTime),Orders::getOrderTime,beginTime,endTime);
        Page<Orders> page1 = ordersService.page(ordersPage, queryWrapper);
        return R.success(page1);

    }

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        ordersService.submit(orders);
        return R.success("下单成功");
    }

}
