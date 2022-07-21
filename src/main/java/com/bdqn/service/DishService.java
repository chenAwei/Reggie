package com.bdqn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bdqn.dto.DishDto;
import com.bdqn.pojo.Category;
import com.bdqn.pojo.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    void saveDishAndFlavor(DishDto dishDto);

    DishDto SelectDishAndFlavor(Long id);

    void updateDishAndFlavor(DishDto dishDto);

    void deleteDishAndFlavor(Long[] ids);
}
