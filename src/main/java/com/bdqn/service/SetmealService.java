package com.bdqn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bdqn.dto.SetmealDto;
import com.bdqn.pojo.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);

    Setmeal SelectWithDish(Long id);

    void updateWithDish(SetmealDto setmealDto);
}
