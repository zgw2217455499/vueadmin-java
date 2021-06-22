package com.zhang.config;

import com.zhang.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
/**
 * EnableWebSecurity注解有两个作用,
 * 1: 加载了WebSecurityConfiguration配置类, 配置安全认证策略。
 * 2: 加载了AuthenticationConfiguration, 配置了认证信息。
 */
@EnableWebSecurity
/**
 * 其中注入了AuthenticationConfiguration配置类，这个类的主要作用是,
 * 向spring容器中注入AuthenticationManagerBuilder,
 * AuthenticationManagerBuilder其实是使用了建造者模式,
 * 他能建造AuthenticationManager,
 * 是身份认证的入口。
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    LoginSuccessHandler loginSuccessHandler;
    @Autowired
    LoginFailureHandler loginFailureHandler;
    @Autowired
    CaptchaFilter captchaFilter;
    //注入 JwtAuthenticationFilter
    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }
    //定义静态资源 和 放行的路径
    private static final String[] URL_WHITELIST = {

            "/login",
            "/logout",
            "/captcha",
            "/favicon.ico"

    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //关闭cors跨站请求伪造
        http.cors()
                .and().csrf().disable()
                //登录配置
                .formLogin()
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                // 禁用session
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //默认配置 授权静态资源和一些路径 其他请求需要认证
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST)
                .permitAll()
                .anyRequest().authenticated()
                //异常处理器 添加认证失败异常处理器和权限不足异常处理器
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                //添加验证码过滤器
                .and()
                .addFilter(jwtAuthenticationFilter())
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
        ;
    }
}
