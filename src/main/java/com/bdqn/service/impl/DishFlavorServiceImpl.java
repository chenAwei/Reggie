package com.bdqn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bdqn.mapper.DishFlavorMapper;
import com.bdqn.pojo.DishFlavor;
import com.bdqn.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper , DishFlavor> implements DishFlavorService {
}
