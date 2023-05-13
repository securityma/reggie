package com.kobe.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kobe.reggie.entity.OrderDetail;
import com.kobe.reggie.mapper.OrderDetailMapper;
import com.kobe.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderdetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
