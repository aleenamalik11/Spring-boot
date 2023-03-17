package com.hazelsoft.springsecurityjpa.config;

import com.hazelsoft.springsecurityjpa.service.UserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hazelsoft.springsecurityjpa.filters.JwtTokenFilter;
import com.hazelsoft.springsecurityjpa.repo.UserRepo;
import com.hazelsoft.springsecurityjpa.service.MyUserDetailsService;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig {
	
	private JwtTokenFilter jwtTokenFilter;
	
	private UserService userService;

	public SecurityConfig(JwtTokenFilter jwtTokenFilter, UserRepo userRepo, UserService userService) {
		this.jwtTokenFilter = jwtTokenFilter;
		this.userService = userService;
	}

	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors().and().authorizeHttpRequests(authorize -> {
					authorize
							.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
							.permitAll()
							.requestMatchers("/login/**", "/register/**")
							.permitAll()
							.requestMatchers("/actuator/**")
							.hasRole("ADMIN")
							.anyRequest()
							.authenticated();
				})
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
				.csrf().disable();
		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new MyUserDetailsService(this.userService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
