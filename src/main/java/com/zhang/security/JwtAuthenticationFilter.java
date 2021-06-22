package com.zhang.security;

import cn.hutool.core.util.StrUtil;
import com.zhang.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    @Autowired
    JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //获取请求头部信息
//        System.err.println("------->"+jwtUtil.getHeader());
        String jwt = request.getHeader(jwtUtil.getHeader());
        //判断jwt是否为空
        if (StrUtil.isBlankOrUndefined(jwt)) {
            chain.doFilter(request, response);
            return;
        }
        //解析jwt
        Claims claims = jwtUtil.getClaimsByToken(jwt);
        if (claims == null) {
            throw new JwtException("token 错误");
        }
        //验证token是否过期
        if(jwtUtil.isTokenExpired(claims)){
            throw new JwtException("token 过期");
        }
        //获取主体 username
        String username = claims.getSubject();
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username, null, null);

        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request, response);
    }
}
