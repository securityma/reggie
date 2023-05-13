package com.kobe.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kobe.reggie.entity.Employee;
import com.kobe.reggie.mapper.EmployeeMapper;
import com.kobe.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements  EmployeeService {
}
