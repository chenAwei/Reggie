package com.bdqn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdqn.common.R;
import com.bdqn.pojo.Category;
import com.bdqn.service.CategoryService;
import com.bdqn.service.impl.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> add(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增成功");
    }

    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize){
        Page<Category> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    @GetMapping("/{id}")
    public R<Category> getById(@PathVariable Long id){
        Category category = categoryService.getById(id);
        return R.success(category);
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> delete(Long ids){
        categoryService.removeById(ids);
        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
