package com.pinyougou.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("login")
public class loginController {

    @RequestMapping("loginName")
    public Map<String,String> getName(){
        //这个中 我们需要的是安全认证框架的登录名的获取
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String,String> map= new HashMap<>();
        map.put("loginName",name);
        return map;
    }
}
