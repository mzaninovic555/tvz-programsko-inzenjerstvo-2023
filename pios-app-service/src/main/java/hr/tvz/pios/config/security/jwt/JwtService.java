package hr.tvz.pios.config.security.jwt;

import hr.tvz.pios.config.PiosProperties;
import hr.tvz.pios.config.security.user.ApplicationUser;
import hr.tvz.pios.config.security.user.UserAuthentication;
import hr.tvz.pios.model.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Servis za baratanje JWT tokenom.
 */
@Service
public class JwtService {

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

  private String jwtSecret;

  private Long jwtValiditySeconds;

  @Autowired
  PiosProperties piosProperties;

  @PostConstruct
  private void postConstruct() {
    this.jwtSecret = piosProperties.getJwt().getSecret();
    this.jwtValiditySeconds = piosProperties.getJwt().getValiditySeconds();
  }

  /**
   * Provjerava je li korisnik validan, te je li unio točne podatke. TRUE - validan korisnik.
   * @param jwtToken {@link String}
   * @return {@link Boolean}
   */
  public Boolean authenticate(String jwtToken) {
    if (isJwtValid(jwtToken)) {
      ApplicationUser applicationUser = getApplicationUserFromJwt(jwtToken);
      saveAuthentication(applicationUser);
      return true;
    }
    return false;
  }

  /**
   * Stvara novi JWT tokena za korisnika koji ga nema ili mu je istekao.
   * @param user - korisnik za kojeg se stvara novi token. {@link User}
   * @return kreirani JWT token, {@link String}
   */
  public String createJwtToken(User user) {
    Instant expiration = Instant.now().plusSeconds(jwtValiditySeconds);
    String authority = user.getRole().getRole();
    return Jwts
        .builder()
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .setHeaderParam("type", "JWT")
        .setSubject(user.getUsername())
        .setExpiration(new Date(expiration.toEpochMilli()))
        .setIssuedAt(new Date())
        .claim("authorities", authority)
        .compact();
  }

  private Boolean isJwtValid(String jwtToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJwt(jwtToken);
      return true;
    } catch (SignatureException e) {
      LOGGER.debug("Invalid JWT signature.");
    } catch (MalformedJwtException e) {
      LOGGER.debug("Invalid JWT token.");
    } catch (UnsupportedJwtException e) {
      LOGGER.debug("Unsupported JWT token.");
    } catch (IllegalArgumentException e) {
      LOGGER.debug("JWT token compact of handler are invalid.");
    }
    return true;
  }

  private ApplicationUser getApplicationUserFromJwt(String jwtToken) {
    Claims claims = Jwts
        .parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(jwtToken)
        .getBody();

    List<SimpleGrantedAuthority> authorities = Arrays
        .stream(claims.get("authorities").toString().split(","))
        .map(SimpleGrantedAuthority::new)
        .toList();

    return ApplicationUser.builder()
        .username(claims.getSubject())
        .authorities(authorities)
        .build();
  }

  private void saveAuthentication(ApplicationUser applicationUser) {
    Authentication authentication = new UserAuthentication(applicationUser);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
