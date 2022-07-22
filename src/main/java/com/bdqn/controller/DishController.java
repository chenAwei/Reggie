package com.bdqn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bdqn.common.R;
import com.bdqn.dto.DishDto;
import com.bdqn.pojo.Category;
import com.bdqn.pojo.Dish;
import com.bdqn.pojo.DishFlavor;
import com.bdqn.pojo.Setmeal;
import com.bdqn.service.CategoryService;
import com.bdqn.service.DishFlavorService;
import com.bdqn.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        Page<Dish> dishPage = new Page<>(page,pageSize);
        Page<DishDto> dishDto = new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage,queryWrapper);
        BeanUtils.copyProperties(dishPage,dishDto,"records");
        List<Dish> records = dishPage.getRecords();
        List<DishDto> list = records.stream().map((item) ->{
            DishDto dishDto1 = new DishDto();
            BeanUtils.copyProperties(item,dishDto1);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto1.setCategoryName(categoryName);
            return dishDto1;
        }).collect(Collectors.toList());
        dishDto.setRecords(list);
        return R.success(dishDto);
    }

    @PostMapping
    public R<String> add(@RequestBody DishDto dishDto){
        String key = "dish_"+dishDto.getCategoryId() + "_" + dishDto.getStatus();
        redisTemplate.delete(key);
        dishService.saveDishAndFlavor(dishDto);
        return R.success("新增菜品成功！");
    }

    @GetMapping("/{id}")
    public R<DishDto> getDishById(@PathVariable Long id){
        DishDto dishDto = dishService.SelectDishAndFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateDishAndFlavor(dishDto);
        return R.success("修改菜品信息成功！");
    }

    @DeleteMapping
    public R<String> delete(Long[] ids){
        dishService.deleteDishAndFlavor(ids);
        return R.success("删除菜单信息成功！");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status,Long[] ids){
        for (Long id : ids) {
            Dish dish = new Dish();
            dish.setStatus(status);
            dish.setId(id);
            dishService.updateById(dish);
        }
        return R.success("更新菜单信息成功！");
    }

//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        String key = "dish_"+dish.getCategoryId() + "_" + dish.getStatus();
//        List<Dish> dishs = (List<Dish>) redisTemplate.opsForValue().get(key);
//        if (dishs != null){
//            return R.success(dishs);
//        }
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
//        List<Dish> list = dishService.list(queryWrapper);
//        redisTemplate.opsForValue().set(key,list,60, TimeUnit.MINUTES);
//        return R.success(list);
//    }

    @GetMapping("/list")
    public R<List<DishDto>> list(DishDto dish){
        String key = "dish_"+dish.getCategoryId() + "_" + dish.getStatus();
        List<DishDto> dishDtos = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if (dishDtos != null){
            return R.success(dishDtos);
        }
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        redisTemplate.opsForValue().set(key,list,60, TimeUnit.MINUTES);
        return R.success(dishDtoList);
    }
}
