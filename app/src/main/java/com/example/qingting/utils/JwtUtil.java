package com.example.qingting.utils;




import com.example.qingting.MyApplication;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

public class JwtUtil {
    private static String JWT_KEY = null;
    private final static Object lock = new Object();
    static String getJwtKey() {
        if (JWT_KEY == null) {
            synchronized (lock) {
                if (JWT_KEY == null)
                    JWT_KEY = ProfileReader.getProperty(MyApplication.getInstance().getApplicationContext(), "JWT_KEY");
            }
        }
        return JWT_KEY;
    }

    /**
     * 解析token，获取过期时间
     * @param token
     * @return
     * @throws JwtException
     */
    public static Date getExpireTime(String token) throws JwtException, NullPointerException {
        if (token == null) {
            throw new NullPointerException();
        }
        Jws<Claims> jwt = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(getJwtKey().getBytes()))
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
