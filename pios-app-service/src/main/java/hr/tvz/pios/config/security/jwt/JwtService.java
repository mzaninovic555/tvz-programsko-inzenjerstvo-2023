package hr.tvz.pios.config.security.jwt;

import hr.tvz.pios.config.PiosProperties;
import hr.tvz.pios.modul.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servis za baratanje JWT tokenom.
 */
@Service
public class JwtService {
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
   * Stvara novi JWT tokena za korisnika koji ga nema ili mu je istekao.
   *
   * @param user - korisnik za kojeg se stvara novi token. {@link User}.
   * @return kreirani JWT token, {@link String}.
   */
  public String createJwtToken(User user) {
    Instant expiration = Instant.now().plusSeconds(jwtValiditySeconds);
    String authority = user.getRole().getRole();
    // TODO multiple roles
    Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

    return Jwts.builder()
        .signWith(key, SignatureAlgorithm.HS512)
        .setHeaderParam("type", "JWT")
        .setSubject(user.getUsername())
        .setExpiration(new Date(expiration.toEpochMilli()))
        .setIssuedAt(new Date())
        .claim("roles", authority)
        .compact();
  }
}
