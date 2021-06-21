package com.zhang.security;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zhang.common.exception.CaptchaException;
import com.zhang.common.lang.Const;
import com.zhang.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CaptchaFilter extends OncePerRequestFilter {
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private RedisUtil redisUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
//        System.err.println("requestURI"+requestURI);
        String method = request.getMethod();
        if ("/login".equals(requestURI) && "POST".equals(method)) {
            //System.out.println("我进入了post");
            try {
                validate(request);
            }catch (CaptchaException e){
                loginFailureHandler.onAuthenticationFailure(request,response,e);
            }
        }
        filterChain.doFilter(request,response);

    }

    private void validate(HttpServletRequest request) {
        String code = request.getParameter("code");

        //存储在redis中的key
        String key = request.getParameter("token");


        //如果为空
        if(StringUtils.isBlank(code) || StringUtils.isBlank(key)){
            throw new CaptchaException("验证码错误");
        }
        //输入验证码与redis中验证码进行比较
        if(!code.equals(redisUtil.hget(Const.CAPTCHA_KEY,key))){
            throw new CaptchaException("验证码错误");
        }

        //只使用一次
        redisUtil.hdel(Const.CAPTCHA_KEY,key);
    }
}
