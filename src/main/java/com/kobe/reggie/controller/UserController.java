package com.kobe.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kobe.reggie.common.R;
import com.kobe.reggie.entity.User;
import com.kobe.reggie.service.UserService;
import com.kobe.reggie.utils.SMSUtils;
import com.kobe.reggie.utils.ValidateCodeUtils;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/user")
@Slf4j
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession httpSession){
        String phone=user.getPhone();

        if(StringUtils.isNotEmpty(phone)){
            String code= ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code:{}",code);

            //SMSUtils.sendMessage("reggie","",phone,code);
            httpSession.setAttribute(phone,code);
            return R.success("发送成功");
        }
        return R.error("验证失败");
    }
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession httpSession){
        String phone=map.get("phone").toString();
        String code=map.get("code").toString();

        Object codeInSession= httpSession.getAttribute(phone);

        if (codeInSession!=null&&codeInSession.equals(code)){
            LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if(user==null){
                user=new User();
                user.setPhone(phone);
                userService.save(user);
            }
            httpSession.setAttribute("user",user.getId());
            return R.success(user);

        }
        return R.error("验证失败");
    }
}
