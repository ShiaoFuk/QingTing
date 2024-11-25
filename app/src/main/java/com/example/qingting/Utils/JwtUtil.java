package com.example.qingting.Utils;

import static javax.crypto.Cipher.SECRET_KEY;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class JwtUtil {


    /**
     * 解析token，获取过期时间
     * @param token
     * @return
     * @throws JwtException
     */
    public static Date getExpireTime(String token) throws JwtException {
        Jws<Claims> jwt = Jwts.parser()
                .build()
                .parseSignedClaims(token);
        Claims claims = jwt.getPayload();
        Date expireTime = claims.getExpiration();
        if (checkIfTokenExpire(expireTime)) {
            throw new JwtException("expired token");
        }
        return expireTime;
    }

    /**
     * 检测token的日期是否过期，过期返回true
     * @param expireTime
     * @return true if expired, else false
     */
    public static boolean checkIfTokenExpire(Date expireTime) {
        return !(expireTime.getTime() < new Date().getTime());
    }
}
