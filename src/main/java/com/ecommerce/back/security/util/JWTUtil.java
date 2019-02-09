package com.ecommerce.back.security.util;

import com.ecommerce.back.security.AuthenticationLevel;
import com.ecommerce.back.security.PersonDetail;
import io.jsonwebtoken.*;

import java.util.Date;

public class JWTUtil {
    private static final long EXPIRED_TIME = 60 * 1000;
    private static final String SECRET = "d20wss@P";
    private static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER_KEY = "Authentication";
    public static final String SPECIFIC_PARAM_NAME = "IndividualName";

    public static String getJWTString(String userName, AuthenticationLevel authenticationLevel, Date expiredTime) {
        expiredTime.setTime(System.currentTimeMillis() + EXPIRED_TIME);
        return TOKEN_PREFIX +
                Jwts.builder()
                .claim("authenticationLevel", authenticationLevel.name)
                .setSubject(userName)
                .setExpiration(expiredTime)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    /**
     * parse jwt String returns PersonDetail
     * @param jwtString jwt String
     * @return PersonDetail
     * @throws ExpiredJwtException if the specified JWT is a Claims JWT and the Claims has an expiration time before the time this method is invoked.
     * @throws UnsupportedJwtException if the {@code claimsJws} argument does not represent an Claims JWS
     * @throws MalformedJwtException if the {@code claimsJws} string is not a valid JWS
     * @throws SignatureException if the {@code claimsJws} JWS signature validation fails
     * @throws IllegalArgumentException if the {@code claimsJws} string is {@code null} or empty or only whitespace
     */
    public static PersonDetail getPersonDetailByJWTString(String jwtString)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        if (jwtString == null) return null;
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(jwtString.replace(TOKEN_PREFIX, ""))
                .getBody();

        if (claims.get("authenticationLevel") instanceof String) {
            try {
                AuthenticationLevel authenticationLevel = AuthenticationLevel.valueOf(
                        (String) claims.get("authenticationLevel")
                );
                String userName = claims.getSubject();
                return new PersonDetail(userName, authenticationLevel);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        else
            return null;
    }
}
