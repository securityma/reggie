package com.kobe.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kobe.reggie.dto.DishDto;
import com.kobe.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);

    void updatewithFlavor(DishDto dishDto);
}
