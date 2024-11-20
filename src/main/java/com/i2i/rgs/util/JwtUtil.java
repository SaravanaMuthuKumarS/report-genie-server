package com.i2i.rgs.util;

import com.i2i.rgs.helper.UnAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String secretKey;
    private static final SecretKey key;
    private static final long EXPIRATION_TIME_MS = 60 * 60 * 500;

    static {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            key = Keys.hmacShaKeyFor(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new JwtException("Error in generating key", e);
        } catch (Exception e) {
            throw new JwtException("error in generating key", e);
        }
    }

    /**
     * <p>
     * Generates Token for every Login.
     * </p>
     *
     * @param username to generate token.
     * @return {@link String} generated Token as a String
     */
    public static String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .and()
                .signWith(key)
                .compact();
    }

    /**
     * <p>
     * Validates the Token with username and checks Expiration.
     * </p>
     *
     * @param token to validate the token.
     * @param userDetails to validate the token.
     * @return {@link Boolean} value true if the token is valid else returns false.
     */
    public static boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUserName(token);
            return ((username.equals(userDetails.getUsername())) && !isTokenExpired(token));
        } catch (Exception e) {
            throw new UnAuthorizedException("Error in validating the token, invalid token");
        }
    }

    /**
     * <p>
     * Extracts the username from the token.
     * </p>
     *
     * @param token to extract the username.
     * @return {@link String} extracted username.
     */
    public static String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * <p>
     * Extracts the claims from the token.
     * </p>
     *
     * @param token to extract the claim.
     */
    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * <p>
     * Checks the expiration of token.
     * </p>
     *
     * @param token to check expiration.
     * @return {@link Boolean} true if the token is not expired else returns false.
     */
    private static boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    /**
     * <p>
     * Extracts the  date of Expiration from the token.
     * </p>
     *
     * @param token to extract the date of Expiration.
     * @return {@link Date} of Expiration.
     */
    private static Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        } catch (Exception e) {
            throw new UnAuthorizedException("Error in extracting expiration from token");
        }
    }

    /**
     * <p>
     * Extracts all the claims of the token
     * </p>
     *
     * @param token to extract all claims.
     * @return {@link Claims} all the claims of the token.
     */
    public static Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new UnAuthorizedException("Invalid token or Token Expired");
        }
    }
}
