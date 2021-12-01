package edu.demian.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  @Value("${jwt.secret.key}")
  private String jwtSecretKey;

  @Value("${jwt.expiration.time.in.minutes}")
  private long jwtExpirationTimeInMinutes;

  @Value("${jwt.header}")
  private String authorizationHeader;

  private final UserDetailsService userDetailsService;

  public JwtTokenProvider(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(authorizationHeader);
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public String createToken(String email) {
    Claims claims = Jwts.claims().setSubject(email);

    LocalDateTime ldt = LocalDateTime.now().plusMinutes(jwtExpirationTimeInMinutes);
    Instant instant = ldt.atZone(ZoneId.systemDefault()).toInstant();
    Date expiration = Date.from(instant);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(new Date())
        .setExpiration(expiration)
        .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
        .compact();
  }

  public boolean isTokenValid(String token) {
    return Jwts.parser()
        .setSigningKey(jwtSecretKey)
        .parseClaimsJws(token)
        .getBody()
        .getExpiration()
        .after(new Date());
  }

  public String getEmail(String token) {
    return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody().getSubject();
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(getEmail(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }
}
