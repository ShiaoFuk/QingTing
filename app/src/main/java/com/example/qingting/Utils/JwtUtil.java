package com.example.qingting.Utils;



import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
    private final static String JWT_KEY = "dc36eb68d08d3dafc853e4bae50d4ad94a4a2093da8d639ada833826177e83ce";
    /**
     * 解析token，获取过期时间
     * @param token
     * @return
     * @throws JwtException
     */
    public static Date getExpireTime(String token) throws JwtException {
        Jws<Claims> jwt = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(JWT_KEY.getBytes()))
                .build()
                .parseSignedClaims(token);
        Claims claims = jwt.getPayload();
        Date expireTime = claims.getExpiration();
        if (checkIfTokenExpire(expireTime)) {
            throw new JwtException("token expired");
        }
        return expireTime;
    }

    /**
     * 检测token的日期是否过期，过期返回true
     * @param expireTime
     * @return true if expired, else false
     */
    public static boolean checkIfTokenExpire(Date expireTime) {
        return (expireTime.getTime() < new Date().getTime());
    }
}
