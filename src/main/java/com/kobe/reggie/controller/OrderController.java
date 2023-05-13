package com.kobe.reggie.controller;

import com.kobe.reggie.common.R;
import com.kobe.reggie.entity.Orders;
import com.kobe.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrdersService ordersService;

    @PostMapping("submit")
    private R<String> submit(@RequestBody  Orders orders)
    {
      ordersService.submit(orders);
      return R.success("支付成功");
    }
}
