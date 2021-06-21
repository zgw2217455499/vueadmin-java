package com.zhang.utils;

import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
@Data
@Component
@ConfigurationProperties(prefix = "zhang.jwt")
public class JwtUtil {
    //过期时间
    private  long expire;
    //秘钥
    private  String secret;
    //头部
    private String header;

    //生成Jwt
    public  String generateToken(String username) {
        Date nowDate = new Date();
        //设置过期时间
        Date expirDate = new Date(nowDate.getTime() + (1000 * expire));
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(nowDate)
                .setExpiration(expirDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    //解析Jwt
    public Claims getClaimsByToken(String jwt) {
        try {
         return  Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
           return null;
        }
    }
    //判断Jwt是否过期
    public boolean isTokenExpired(Claims claims){
        return claims.getExpiration().before(new Date());
    }
}
