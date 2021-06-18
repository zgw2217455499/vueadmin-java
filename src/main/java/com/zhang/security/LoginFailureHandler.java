package com.zhang.security;

import cn.hutool.json.JSONUtil;
import com.zhang.common.lang.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setContentType("UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        Result fail = Result.fail(e.getMessage());
        outputStream.write(JSONUtil.toJsonStr(fail).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}
