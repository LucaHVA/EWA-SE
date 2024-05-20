package org.example.backend.security;



import io.jsonwebtoken.*;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;




import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
public class JWToken {

    private String callName;
    private Long userId;
    private String role;
    private static final String JWT_ISSUER_CLAIM = "iss";
    private static final String JWT_CALLNAME_CLAIM = "sub";
    private static final String JWT_USERID_CLAIM = "id";
    private static final String JWT_ROLE_CLAIM = "role";
    public static final String JWT_ATTRIBUTE_NAME = "JWTokenInfo";

    public JWToken(String callName, Long userId, String role) {
        this.callName = callName;
        this.userId = userId;
        this.role = role;
    }




    public String encode(String issuer, String passphrase, int expiration) {
        Key key = getKey(passphrase);

        return Jwts.builder()
                .claim(JWT_CALLNAME_CLAIM,this.callName)
                .claim(JWT_USERID_CLAIM,this.userId)
                .claim(JWT_ROLE_CLAIM, this.role)
                .setIssuer(issuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiration*1000L))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private static Key getKey(String passphrase) {
        byte[] hmacKey= passphrase.getBytes(StandardCharsets.UTF_8);
      return new SecretKeySpec(hmacKey, SignatureAlgorithm.HS512.getJcaName());
    }



    public static JWToken decode(String token, String issuer, String passphrase)
            throws ExpiredJwtException, MalformedJwtException {
        // Validate the token string and extract the claims
        Key key = getKey(passphrase);
        Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token);
        Claims claims = jws.getBody();

        if (!claims.get(JWT_ISSUER_CLAIM).toString().equals(issuer)) {
            throw new MalformedJwtException("Invalid issuer");
        }
        // build our token from the extracted claims
        JWToken jwToken = new JWToken(
                claims.get(JWT_CALLNAME_CLAIM).toString(),
                Long.valueOf(claims.get(JWT_USERID_CLAIM).toString()),
                claims.get(JWT_ROLE_CLAIM).toString()
        );
        return jwToken;
    }
//
//    public static final String JWT_ATTRIBUTE_NAME = "JWTokenInfo";
//
//
//
//
//    public String getCallName() {
//        return callName;
//    }
//
//    public void setCallName(String callName) {
//        this.callName = callName;
//    }
//
//    public Long getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Long userId) {
//        this.userId = userId;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }


}

