package com.kobe.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kobe.reggie.common.R;
import com.kobe.reggie.dto.DishDto;
import com.kobe.reggie.entity.Category;
import com.kobe.reggie.entity.Dish;
import com.kobe.reggie.entity.DishFlavor;
import com.kobe.reggie.entity.Employee;
import com.kobe.reggie.service.CategoryService;
import com.kobe.reggie.service.DishFlavorService;
import com.kobe.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/dish")
@RestController
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private  CategoryService categoryService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private RedisTemplate redisTemplate;
    @GetMapping("/{id}")
    public R<Dish> getById(@PathVariable  Long id){
        Dish emp= dishService.getById(id);
        if(emp!=null){
            return  R.success(emp);
        }
        return  R.error("NONE");
    }
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
        return R.success("添加成功");
    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto)
    {
        dishService.updatewithFlavor(dishDto);
        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
        return R.success("修改成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name)
    {
        log.info("page ={} pagesize={},name={}",page,pageSize,name);
        Page<Dish> pageInfo=new Page<>(page,pageSize);
        Page<DishDto> pageDtoInfo=new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.isNotEmpty(name),
                Dish::getName,
                name);

        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo,queryWrapper);
        BeanUtils.copyProperties(pageInfo,pageDtoInfo,"records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list=records.stream().map((item)->{
            DishDto dishDto=new DishDto();

            BeanUtils.copyProperties(item,dishDto);
           Long categoryId= item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList());

        pageDtoInfo.setRecords(list);
        return R.success(pageDtoInfo);
    }
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        LambdaQueryWrapper<Dish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        lambdaQueryWrapper.eq(Dish::getStatus,1);
//        List<Dish> list = dishService.list(lambdaQueryWrapper);
//        return  R.success(list) ;
//    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        List<DishDto> dishDtos=null;
        //redis缓存中查询
        String key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        dishDtos=(List<DishDto>) redisTemplate.opsForValue().get(key);
        if(dishDtos!=null){
            return R.success(dishDtos);
        }
        //不存在
        LambdaQueryWrapper<Dish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(lambdaQueryWrapper);
        dishDtos=list.stream().map((item)->{
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long id = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper();
            queryWrapper.eq(DishFlavor::getDishId,id);
            List<DishFlavor> list1 = dishFlavorService.list(queryWrapper);
            dishDto.setFlavors(list1);
            return dishDto;
        }).collect(Collectors.toList());
        //不存在则添加进redis
        redisTemplate.opsForValue().set(key,dishDtos,30, TimeUnit.MINUTES);
        return  R.success(dishDtos) ;
    }
}
