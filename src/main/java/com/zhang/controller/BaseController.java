package com.zhang.controller;

import com.zhang.common.lang.Result;
import com.zhang.entity.SysUser;
import com.zhang.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BaseController {
    @Autowired
    SysUserService sysUserService;
    @GetMapping("/test")
    public Result test(){
//        int a = 5/0;
        List<SysUser> list = sysUserService.list();
        return Result.succ(list);
    }
}
