package com.whisky.blogrestapi.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.whisky.blogrestapi.config.CustomUserDetailsService;
import com.whisky.blogrestapi.payload.UnAuthorizedResponse;
import com.whisky.blogrestapi.security.JwtTokenProvider;
import com.whisky.blogrestapi.utils.AuthResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private UnAuthorizedResponse unAuthorizedResponse;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		// get JWT token from HTTP request
		String token = getJWTfromRequest(request);

		if (token != null) {
			
			// validate token
			if (StringUtils.hasText(token) && tokenProvider.validateToken(token,request)) {

				// get username from token
				String username = tokenProvider.getUsernameFromJWT(token);

				// load userDetails from the username
				UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails.getUsername(), null, userDetails.getAuthorities());
				// set Spring security
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}else {
			AuthResponseUtil.setResponseDataToUnAuthorizationBean("JWT token not found", request, unAuthorizedResponse);
		}
		
		filterChain.doFilter(request, response);

	}

	// Bearer <accessToken>
	private String getJWTfromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

}
