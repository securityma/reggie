package com.kobe.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kobe.reggie.common.R;
import com.kobe.reggie.entity.Category;
import com.kobe.reggie.entity.Employee;
import com.kobe.reggie.service.CategoryService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/category")
@RestController
@Slf4j
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("创建成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize)
    {
        log.info("page ={} pagesize={},name={}",page,pageSize);
        Page<Category> pageInfo=new Page<>(page,pageSize);

        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper();

        queryWrapper.orderByDesc(Category::getSort);

        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    @DeleteMapping
    public  R<String> delete(Long ids){
        log.info("id = {}");
        categoryService.remove(ids);

        return R.success("成功删除分类");
    }
    @PutMapping
    public R<String> update(@RequestBody  Category category)
    {
        categoryService.updateById(category);
        return R.success("修改成功");
    }
    @RequestMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());

        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list=categoryService.list(queryWrapper);

        return R.success(list);

    }
}

