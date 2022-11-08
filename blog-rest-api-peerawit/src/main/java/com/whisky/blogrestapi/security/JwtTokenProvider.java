package com.whisky.blogrestapi.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.whisky.blogrestapi.payload.UnAuthorizedResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	@Value("${app.jwt-secret}")
	private String jwtSecret;

	@Value("${app.jwt-expiration-milliseconds}")
	private int jwtExpirationInMs;

	@Autowired
	private UnAuthorizedResponse unAuthorizedResponse;

	private SecretKey getKey() {
		return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}

	// generate token
	public String generateToken(Authentication authentication) {

		String jwt = Jwts.builder().setIssuer("Peerawit System").setSubject("JWT Token")
				.claim("username", authentication.getName()).setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + 14400000)).signWith(getKey())
				.compact();
		return jwt;
	}

	// get username from the token
	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token)
				.getBody();
		String username = (String) claims.get("username");
		return username;
	}

	// validate JWT token
	public boolean validateToken(String token, HttpServletRequest request) {

		try {

			SecretKey key = Keys // generate key using the secret
					.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SignatureException ex) {
			setResponseDataToUnAuthorizationBean("Invalid JWT signature", request);
		} catch (MalformedJwtException ex) {
			setResponseDataToUnAuthorizationBean("Invalid JWT token", request);
		} catch (ExpiredJwtException ex) {
			setResponseDataToUnAuthorizationBean("Expired JWT Token", request);
		} catch (UnsupportedJwtException ex) {
			setResponseDataToUnAuthorizationBean("Unsuppported JWT Token", request);
		} catch (IllegalArgumentException ex) {
			setResponseDataToUnAuthorizationBean("JWT Claim string is empty", request);
		}
		return false;
	}

	private void setResponseDataToUnAuthorizationBean(String message, HttpServletRequest request) {
		unAuthorizedResponse.setTimestamp(new Date());
		unAuthorizedResponse.setMessage(message);
		unAuthorizedResponse.setDetails("uri=" + request.getRequestURI());
		throw new RuntimeException();
	}

}
