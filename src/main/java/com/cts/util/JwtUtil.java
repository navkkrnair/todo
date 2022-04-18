package com.cts.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
	private static final String SECRET = "secret";

	public String generateToken(Authentication authentication) {
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("name", authentication.getName());
		claims.put("roles", authentication.getAuthorities());

		Instant issuedAt = Instant.now()
				.truncatedTo(ChronoUnit.SECONDS);
		Instant expiration = issuedAt.plus(1, ChronoUnit.MINUTES);

		log.info("Creating JWT token");
		return Jwts.builder()
				.setClaims(claims)
				.setIssuer("cts.com")
				.setSubject(authentication.getName())
				.setIssuedAt(Date.from(issuedAt))
				.setExpiration(Date.from(expiration))
				.signWith(SignatureAlgorithm.HS256, SECRET)
				.compact();
	}

	public String extractUsername(String token) {
		try {
			return Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
				| IllegalArgumentException e) {
			log.info(e.getMessage());
			throw e;
		}
	}

}
