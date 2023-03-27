package com.hazelsoft.springsecurityjpa.filters;

import java.io.IOException;

import com.hazelsoft.springsecurityjpa.exception.CustomException;
import com.hazelsoft.springsecurityjpa.exception.RestResponseExceptionResolver;
import io.jsonwebtoken.SignatureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelsoft.springsecurityjpa.service.JwtService;
import com.hazelsoft.springsecurityjpa.service.MyUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	private MyUserDetailsService userDetailsService;
	
	private JwtService jwtService;
	
	private ObjectMapper objectMapper;

	private RestResponseExceptionResolver exceptionResolver;

	public JwtTokenFilter(MyUserDetailsService userDetailsService, JwtService jwtService,
						  ObjectMapper objectMapper, RestResponseExceptionResolver exceptionResolver) {
		this.userDetailsService = userDetailsService;
		this.jwtService = jwtService;
		this.objectMapper = objectMapper;
		this.exceptionResolver = exceptionResolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
			FilterChain filterChain)
			throws ServletException, IOException {
	try {
		String jwt = parseJwt(request);
		if (jwt != null) {
			String username = jwtService.extractUsername(jwt);

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			if (jwtService.isTokenValid(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(userDetails, null, 
								userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource()
						.buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);

			}
		}
		}
			filterChain.doFilter(request, response);

		} catch (ExpiredJwtException ex) {
			exceptionResolver.resolveException((HttpServletRequest) request, (HttpServletResponse) response, null, ex);

		} catch(SignatureException ex){
			exceptionResolver.resolveException((HttpServletRequest) request, (HttpServletResponse) response, null, ex);
		}
		catch (Exception ex) {
			exceptionResolver.resolveException((HttpServletRequest) request, (HttpServletResponse) response, null,
					(CustomException) ex);
		}
	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}

		return null;
	}

}
