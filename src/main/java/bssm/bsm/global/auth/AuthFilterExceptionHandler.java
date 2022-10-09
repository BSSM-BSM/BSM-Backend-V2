package bssm.bsm.global.auth;

import bssm.bsm.global.error.HttpError;
import bssm.bsm.global.error.HttpErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthFilterExceptionHandler extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(req, res);
        } catch (HttpError e) {
            exceptionHandler(res, e);
        }
    }

    private void exceptionHandler(HttpServletResponse res, HttpError exception) {
        res.setStatus(exception.getStatusCode());
        res.setContentType("application/json;charset=UTF-8");
        try {
            res.getWriter().write(objectMapper.writeValueAsString(new HttpErrorResponse(exception)));
            res.getWriter().flush();
            res.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
