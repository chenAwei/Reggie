package com.bdqn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bdqn.pojo.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
