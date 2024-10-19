package backend.animal_profiling.Security;

import backend.animal_profiling.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtHelper {


    @Value("${jwt.secret}")  // Load from configuration (application.properties or environment variable)
    private String secretKey;

    @Value("${jwt.expiration}")  // Load token expiration time from configuration
    private long jwtExpirationInMs;

    public JwtHelper(CustomUserDetailsService customUserDetailsService) {
    }

    /**
     * Generate a JWT token based on UserDetails.
     *
     * @param userDetails the user details from which to generate the token.
     * @return a signed JWT token.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());


        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())  // Set the username as the subject
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))  // Set expiration time
                .signWith(SignatureAlgorithm.HS512, secretKey)  // Use HS512 for stronger encryption
                .compact();
    }

    /**
     * Validate the token based on username and expiration.
     *
     * @param token the JWT token.
     * @param userDetails the user details to validate against.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Extract the username from the token.
     *
     * @param token the JWT token.
     * @return the username stored in the token.
     */
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * Check if the token is expired.
     *
     * @param token the JWT token.
     * @return true if the token has expired, false otherwise.
     */
    public boolean isTokenExpired(String token) {
        return getClaimsFromToken(token).getExpiration().before(new Date());
    }

    /**
     * Get all claims from the token.
     *
     * @param token the JWT token.
     * @return the claims present in the token.
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extract the token from the Authorization header.
     *
     * @param request the HttpServletRequest object.
     * @return the JWT token if present, null otherwise.
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}