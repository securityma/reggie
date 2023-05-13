package com.kobe.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kobe.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
