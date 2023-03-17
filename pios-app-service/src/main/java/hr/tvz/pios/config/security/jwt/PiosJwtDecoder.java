package hr.tvz.pios.config.security.jwt;

import com.nimbusds.jwt.JWTParser;
import hr.tvz.pios.config.PiosProperties;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

/**
 * Klasa za pretvaranje JWT Stringa u JWT klasu.
 */
@Component
public class PiosJwtDecoder implements JwtDecoder {
  private final PiosProperties piosProperties;

  private static final Logger LOGGER = LoggerFactory.getLogger(PiosJwtDecoder.class);

  public PiosJwtDecoder(PiosProperties piosProperties) {
    this.piosProperties = piosProperties;
  }

  @Override
  public Jwt decode(String token) {
    RuntimeException caughtException = null;

    try {
      Jwts.parserBuilder()
          .setSigningKey(
              Keys.hmacShaKeyFor(Decoders.BASE64.decode(piosProperties.jwt().secret())))
          .build()
          .parseClaimsJws(token);
    } catch (ExpiredJwtException e) {
      caughtException = e;
      LOGGER.debug("Expired JWT token.");
    } catch (UnsupportedJwtException e) {
      caughtException = e;
      LOGGER.debug("Unsupported JWT token.");
    } catch (MalformedJwtException e) {
      caughtException = e;
      LOGGER.debug("Invalid JWT token.");
    } catch (SignatureException e) {
      caughtException = e;
      LOGGER.debug("JWT token signature invalid.");
    } catch (IllegalArgumentException e) {
      caughtException = e;
      LOGGER.debug("JWT token claims is null/empty.");
    }

    // If an exception occurred throw JwtException (ends authentication)
    if (caughtException != null) {
      throw new JwtException("Invalid JWT - " + caughtException.getMessage());
    }

    try {
      var parsedJwt = JWTParser.parse(token);
      var headers = new LinkedHashMap<>(parsedJwt.getHeader().toJSONObject());
      var claims = new LinkedHashMap<>(parsedJwt.getJWTClaimsSet().getClaims());
      if (claims.get(JwtClaimNames.IAT) instanceof Date iat) {
        claims.put(JwtClaimNames.IAT, iat.toInstant());
      }
      if (claims.get(JwtClaimNames.EXP) instanceof Date exp) {
        claims.put(JwtClaimNames.EXP, exp.toInstant());
      }
      return Jwt.withTokenValue(parsedJwt.getParsedString())
          .headers(h -> h.putAll(headers))
          .claims(c -> c.putAll(claims))
          .build();
    } catch (ParseException e) {
      throw new JwtException("Provided token is not valid");
    }
  }
}
