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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

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
                .httpBasic().disable()
                .cors().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth/oauth/bsm").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/meal/*", "/timetable/*/*", "/banner").permitAll()
                .antMatchers(HttpMethod.POST, "/meister/detail").authenticated()
                .antMatchers(HttpMethod.GET, "/meister/ranking/*").authenticated()
                .antMatchers().authenticated()
                .antMatchers("/meister/**").hasAuthority("STUDENT")
                .antMatchers(HttpMethod.GET, "/board/**", "/post/**", "/comment/**").permitAll()
                .antMatchers(HttpMethod.GET, "/lost-found/find/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().disable();

        http
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authFilterExceptionHandler, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}