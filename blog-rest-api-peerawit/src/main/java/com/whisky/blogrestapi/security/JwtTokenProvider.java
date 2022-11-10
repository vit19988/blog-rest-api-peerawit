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
import com.whisky.blogrestapi.utils.AuthResponseUtil;

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
			AuthResponseUtil.setResponseDataToUnAuthorizationBean("Invalid JWT signature", request,unAuthorizedResponse);
			throw new RuntimeException();
		} catch (MalformedJwtException ex) {
			AuthResponseUtil.setResponseDataToUnAuthorizationBean("Invalid JWT Token", request,unAuthorizedResponse);
			throw new RuntimeException();
		} catch (ExpiredJwtException ex) {
			AuthResponseUtil.setResponseDataToUnAuthorizationBean("Expired JWT Token", request,unAuthorizedResponse);
			throw new RuntimeException();
		} catch (UnsupportedJwtException ex) {
			AuthResponseUtil.setResponseDataToUnAuthorizationBean("Unsuppported JWT Token", request,unAuthorizedResponse);
			throw new RuntimeException();
		} catch (IllegalArgumentException ex) {
			AuthResponseUtil.setResponseDataToUnAuthorizationBean("JWT Claim string is empty", request,unAuthorizedResponse);
			throw new RuntimeException();
		}catch(RuntimeException ex) {
			AuthResponseUtil.setResponseDataToUnAuthorizationBean("XXXX", request,unAuthorizedResponse);
			throw new RuntimeException();

		}

	}


}
