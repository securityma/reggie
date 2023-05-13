package com.kobe.reggie.dto;

import com.kobe.reggie.entity.Setmeal;
import com.kobe.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
