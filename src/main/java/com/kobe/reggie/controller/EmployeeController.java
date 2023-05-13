package com.kobe.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kobe.reggie.common.R;
import com.kobe.reggie.entity.Employee;
import com.kobe.reggie.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest servletRequest, @RequestBody Employee employee)
    {

        String password = employee.getPassword();
        password=DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(employeeLambdaQueryWrapper);

        if(emp==null)
        {
            return R.error("login failed");
        }
        if(!emp.getPassword().equals(password))
        {
            return R.error("login failed");
        }
        if(emp.getStatus().equals(0))
        {
            return R.error("status fail");
        }
        servletRequest.getSession().setAttribute("Emp",emp.getId());
        log.info("login succeeded");
        return  R.success(emp);
    }
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request)
    {
        request.getSession().removeAttribute("Emp");
        return R.success("退出成功");
    }
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        //employee.setCreateTime(LocalDateTime.now());
       // employee.setUpdateTime(LocalDateTime.now());
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        Long eid= (Long)request.getSession().getAttribute("Emp");
     //   employee.setCreateUser(eid);
      //  employee.setUpdateUser(eid);

        employeeService.save(employee);

        return  R.success("add employee succeed");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name)
    {
        log.info("page ={} pagesize={},name={}",page,pageSize,name);
        Page pageInfo=new Page(page,pageSize);

        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.isNotEmpty(name),
                Employee::getName,
                name);

        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo,queryWrapper);


        return R.success(pageInfo);
    }
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee)
    {
        Long eid=(Long)request.getSession().getAttribute("Emp");
    //    employee.setUpdateTime(LocalDateTime.now());
      //  employee.setUpdateUser(eid);
        employeeService.updateById(employee);

        return  R.success("成功修改");
    }
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable  Long id){
        Employee emp = employeeService.getById(id);
        if(emp!=null){
            return  R.success(emp);
        }
        return  R.error("NONE");
    }
}