package com.example.QuanLyNhaXe.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.QuanLyNhaXe.configuration.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.QuanLyNhaXe.util.ResponseMessage;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
	private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private static final String ADMIN = "ADMIN";
    private static final String CUSTOMER="CUSTOMER";
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/auth/**","/infomation/**","/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/v2/api-docs/**",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/swagger-resources/**",
                                "/configuration/security",
                                "/user/edit",
                                "/routes/**",
                                "/upload",
                                "trips",
                                "bookings/**",
                                "bookings",
                                "tickets/*",
                                "/seat-map",
                                "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET,"/qlnx/**").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.POST,"/auth/signup/staff","/auth/signup/driver","/station/**").hasAuthority(ADMIN)
                        .anyRequest().authenticated()
                		)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(customAuthenticationEntryPoint())
                )
                
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
        private AuthenticationEntryPoint customAuthenticationEntryPoint() {
            return (request, response, authException) -> {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                ResponseMessage responseMessage = new ResponseMessage("Access Denied");
                response.getWriter().write(new ObjectMapper().writeValueAsString(responseMessage));
            };
        }

}
