package com.hazelsoft.springsecurityjpa.filters;

import java.io.IOException;

import com.hazelsoft.springsecurityjpa.dto.Status;
import io.jsonwebtoken.SignatureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelsoft.springsecurityjpa.dto.RequestResponse;
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

	public JwtTokenFilter(MyUserDetailsService userDetailsService, JwtService jwtService, 
			ObjectMapper objectMapper) {
		this.userDetailsService = userDetailsService;
		this.jwtService = jwtService;
		this.objectMapper = objectMapper;
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

		} catch (ExpiredJwtException e) {
			logger.warn("the token is expired and not valid anymore", e);
			RequestResponse errorResponse = new RequestResponse(Status.ERROR,
					"the token is expired and not valid anymore", null, e);
			response.setContentType("application/json");
			response.setStatus(403);
			response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
		} catch(SignatureException e){
			logger.warn("Invalid token", e);
			RequestResponse errorResponse = new RequestResponse(Status.ERROR,
					"Invalid token", null, e);
			response.setContentType("application/json");
			response.setStatus(403);
			response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
		}
		catch (Exception e) {
			logger.warn("Error occurred while authentication", e);
			RequestResponse errorResponse = new RequestResponse(Status.ERROR,
					"Error occurred while authentication", null, e);
			response.setContentType("application/json");
			response.setStatus(403);
			response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
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
