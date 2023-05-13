package com.kobe.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kobe.reggie.dto.SetmealDto;
import com.kobe.reggie.entity.Setmeal;
import com.kobe.reggie.entity.SetmealDish;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);
    void removeWithDish(List<Long> ids);
}
