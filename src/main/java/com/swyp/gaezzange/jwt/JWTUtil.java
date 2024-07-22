package com.swyp.gaezzange.jwt;

import static com.swyp.gaezzange.util.DateUtil.getStartOfWeek;

import com.swyp.gaezzange.domain.user.auth.repository.UserAuth;
import com.swyp.gaezzange.domain.user.repository.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JWTUtil {

  @Value("${jwt.secretKey}")
  private String secretKeyString;

  private SecretKey secretKey;

  @Value("${jwt.refreshTokenExpirationTime}")
  private Long refreshTokenExpirationTime;

  @PostConstruct
  protected void init() {
    secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
  }

  public Jws<Claims> parseClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token);
  }

  public List<GrantedAuthority> getAuthorities(String role) {
    GrantedAuthority authority = new SimpleGrantedAuthority(role);
    return Collections.singletonList(authority);
  }

  public boolean isExpired(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody()
        .getExpiration().before(new Date());
  }

  public boolean isNotOnboarded(String token) {
    Long userId = parseClaims(token).getBody().get("userId", Long.class);
    log.debug("userId from token: {}", userId);
    return userId == null;
  }

  public boolean isPastWeekToken(String token) {
    LocalDateTime expiration = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody()
        .getExpiration().toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
    return expiration.isBefore(getStartOfWeek().atStartOfDay());
  }

  public Long getUserAuthId(String token) {
    Long userAuthId = parseClaims(token).getBody().get("userAuthId", Long.class);
    log.debug("userAuthId from token: {}", userAuthId);
    return userAuthId;
  }

  public Long getUserUserId(String token) {
    Long userId = parseClaims(token).getBody().get("userId", Long.class);
    log.debug("userId from token: {}", userId);
    return userId;
  }

  public String createJwt(String category, Long userAuthId, Long userId, String email, String role,
      String provider) {
    return Jwts.builder()
        .claim("category", category)
        .claim("userAuthId", userAuthId)
        .claim("userId", userId)
        .claim("email", email)
        .claim("role", role)
        .claim("provider", provider)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
        .signWith(secretKey)
        .compact();
  }

  public String createJwt(String category, UserAuth userAuth) {
    return Jwts.builder()
        .claim("category", category)
        .claim("userAuthId", userAuth.getUserAuthId())
        .claim("userId", Optional.ofNullable(userAuth).map(UserAuth::getUser).map(User::getUserId).orElse(null))
        .claim("email", userAuth.getEmail())
        .claim("role", userAuth.getRole())
        .claim("provider", userAuth.getProvider())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
        .signWith(secretKey)
        .compact();
  }

//
//    public boolean validateToken(String token, SecretKey secretKey) {
//        try {
//            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
//            return !claims.getBody().getExpiration().before(new Date());
//        } catch (Exception e) {
//            return false;
//        }
//    }
}
