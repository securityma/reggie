package com.kobe.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kobe.reggie.entity.Orders;
import org.springframework.core.annotation.Order;

public interface OrdersService extends IService<Orders> {

    public  void submit(Orders orders);
}
