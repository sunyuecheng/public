package com.sct.springsecuritytest.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class JwtTokenUtil {
    public static String createToken(String audience, Map<String,Object> claimMap,
                                     String subject, String issuer,
                                     long ttl, String secretKey) throws Exception {
        Date createDate = new Date();
        Date expiresDate = createDate;

        if (ttl >= 0) {
            expiresDate = new Date(createDate.getTime() + ttl);
        }

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        JwtBuilder builder = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setAudience(audience)
                .setClaims(claimMap)
                .setSubject(subject)
                .setIssuer(issuer)
                .setIssuedAt(createDate)
                .setExpiration(expiresDate)
                .signWith(signatureAlgorithm, secretKey);
        return builder.compact();



    }

    public static Claims parseToken(String jwtToken,String secretKey) throws Exception {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken)
                .getBody();
    }
}
