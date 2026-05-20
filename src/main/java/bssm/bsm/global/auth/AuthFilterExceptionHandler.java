package bssm.bsm.global.auth;

import bssm.bsm.global.error.HttpException;
import bssm.bsm.global.error.HttpErrorResponse;
import bssm.bsm.global.error.ValidationErrorResponse;
import bssm.bsm.global.error.exceptions.BadRequestException;
import bssm.bsm.global.error.exceptions.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthFilterExceptionHandler extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) {
        try {
            filterChain.doFilter(req, res);
        } catch (BadRequestException e) {
            exceptionHandler(res, e);
        } catch (HttpException e) {
            exceptionHandler(res, e);
        } catch (Exception e) {
            exceptionHandler(res);
        }
    }

    private void exceptionHandler(HttpServletResponse res, HttpException exception) {
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(exception.getStatusCode());
        try {
            res.getWriter().write(objectMapper.writeValueAsString(new HttpErrorResponse(exception)));
            res.getWriter().flush();
            res.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exceptionHandler(HttpServletResponse res, BadRequestException exception) {
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(exception.getStatusCode());
        try {
            res.getWriter().write(objectMapper.writeValueAsString(new ValidationErrorResponse(exception.getFields())));
            res.getWriter().flush();
            res.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exceptionHandler(HttpServletResponse res) {
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            res.getWriter().write(objectMapper.writeValueAsString(new HttpErrorResponse(new InternalServerException())));
            res.getWriter().flush();
            res.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
