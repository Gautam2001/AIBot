package com.AIBot.Utility.Security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.AIBot.Utility.CommonUtils;


@Configuration
public class SecurityConfig {

	@Autowired
	private JwtUtil jwtUtil;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    CommonUtils.logMethodEntry(this);
	    return http
	            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
	            .csrf(csrf -> csrf.disable())
	            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	            .authorizeHttpRequests(auth -> auth
	                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	                    .requestMatchers("/bot/ping").permitAll()
	                    .anyRequest().authenticated())
	            .exceptionHandling(e -> e
	                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
	                    .accessDeniedHandler(new CustomAccessDeniedHandler()))
	            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), 
	                    UsernamePasswordAuthenticationFilter.class)
	            .build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CommonUtils.logMethodEntry(this);
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(
				List.of("http://localhost:5173", "http://localhost:4173", "https://messenger-chats.vercel.app"));

		configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "OPTIONS"));
		configuration.addAllowedHeader("*");

		configuration.setAllowCredentials(true); // needed for WebSocket cookies/token auth

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
