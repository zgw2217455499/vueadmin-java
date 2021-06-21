package com.zhang.controller;

import com.zhang.common.lang.Result;
import com.zhang.entity.SysUser;
import com.zhang.service.SysUserService;
import com.zhang.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

public class BaseController {
    @Autowired
    RedisUtil redisUtil;



}
