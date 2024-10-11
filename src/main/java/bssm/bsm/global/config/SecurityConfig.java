package bssm.bsm.global.config;

import bssm.bsm.global.auth.AuthFilterExceptionHandler;
import bssm.bsm.global.error.exceptions.ForbiddenException;
import bssm.bsm.global.auth.AuthFilter;
import bssm.bsm.global.error.HttpErrorResponse;
import bssm.bsm.global.error.exceptions.UnAuthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthFilter jwtAuthFilter;
    private final AuthFilterExceptionHandler authFilterExceptionHandler;
    private final ObjectMapper objectMapper;

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (req, res, e) -> {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter().write(objectMapper.writeValueAsString(new HttpErrorResponse(new UnAuthorizedException())));
            res.getWriter().flush();
            res.getWriter().close();
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (req, res, e) -> {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter().write(objectMapper.writeValueAsString(new HttpErrorResponse(new ForbiddenException())));
            res.getWriter().flush();
            res.getWriter().close();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement((configure -> {
                    configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }))
                .exceptionHandling(configure -> {
                    configure.authenticationEntryPoint(authenticationEntryPoint());
                    configure.accessDeniedHandler(accessDeniedHandler());
                })
                .authorizeHttpRequests(configure -> {
                    configure.requestMatchers(HttpMethod.POST, "/auth/oauth/bsm").permitAll();
                    configure.requestMatchers("/admin/**").hasAuthority("ADMIN");
                    configure.requestMatchers(HttpMethod.GET, "/meal/*", "/timetable/*/*", "/banner").permitAll();
                    configure.requestMatchers(HttpMethod.POST, "/meister/detail").authenticated();
                    configure.requestMatchers(HttpMethod.GET, "/meister/ranking/*").authenticated();
                    configure.requestMatchers("/meister/**").hasAuthority("STUDENT");
                    configure.requestMatchers(HttpMethod.GET, "/board/**", "/post/**", "/comment/**").permitAll();
                    configure.requestMatchers(HttpMethod.GET, "/lost-found/find/**").permitAll();
                    configure.anyRequest().authenticated();
                })
                .formLogin(AbstractHttpConfigurer::disable);

        http
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authFilterExceptionHandler, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}