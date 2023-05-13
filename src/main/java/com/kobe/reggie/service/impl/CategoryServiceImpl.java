package com.kobe.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kobe.reggie.common.CustomException;
import com.kobe.reggie.entity.Category;
import com.kobe.reggie.entity.Dish;
import com.kobe.reggie.entity.Setmeal;
import com.kobe.reggie.mapper.CategoryMapper;
import com.kobe.reggie.service.CategoryService;
import com.kobe.reggie.service.DishService;
import com.kobe.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dish::getCategoryId,id);
       long count = dishService.count(lambdaQueryWrapper);
        //查询分类是否关联菜品，关联抛出异常
        if(count>0){
            throw new CustomException("cat relative dish");
        }
        //分类是否关联套餐，关联抛出异常
        LambdaQueryWrapper<Setmeal> meallambdaQueryWrapper=new LambdaQueryWrapper<>();
        meallambdaQueryWrapper.eq(Setmeal::getCategoryId,id);

        long count2 = setmealService.count(meallambdaQueryWrapper);
        //查询分类是否关联菜品，关联抛出异常
        if(count2>0){
                throw new CustomException("cat relative meal");
        }
        //正常删除
        super.removeById(id);
    }
}
