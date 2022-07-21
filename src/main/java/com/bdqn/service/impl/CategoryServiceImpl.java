package com.bdqn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bdqn.mapper.CategoryMapper;
import com.bdqn.pojo.Category;
import com.bdqn.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
