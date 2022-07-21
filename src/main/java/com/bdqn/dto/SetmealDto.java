package com.bdqn.dto;

import com.bdqn.pojo.Setmeal;
import com.bdqn.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
