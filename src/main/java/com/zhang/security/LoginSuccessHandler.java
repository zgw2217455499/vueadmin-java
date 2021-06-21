package com.zhang.security;

import cn.hutool.json.JSONUtil;
import com.zhang.common.lang.Result;
import com.zhang.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        //生成jwt放入请求头中
        String jwt = jwtUtil.generateToken(authentication.getName());
        response.setHeader(jwtUtil.getHeader(), jwt);

        Result succ = Result.succ("成功");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(JSONUtil.toJsonStr(succ).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}
