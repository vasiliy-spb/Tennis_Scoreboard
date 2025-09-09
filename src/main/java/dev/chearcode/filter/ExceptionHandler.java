package dev.chearcode.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.chearcode.exception.EntityNotFoundException;
import dev.chearcode.exception.ValidationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebFilter("/*")
public class ExceptionHandler extends HttpFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            super.doFilter(req, res, chain);
        } catch (Exception e) {
            handleException(e, req, res);
        }
    }

    private void handleException(Exception e, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        if (e instanceof ValidationException) {
            req.setAttribute("errorMessage", e.getMessage());
            String path = req.getServletPath() + ".jsp";
            RequestDispatcher dispatcher = req.getRequestDispatcher(path);
            dispatcher.forward(req, res);
        }

        int statusCode = getStatusCode(e);
        res.setStatus(statusCode);

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", e.getMessage());
        objectMapper.writeValue(res.getWriter(), errorResponse);
    }

    private int getStatusCode(Exception e) {
        if (e instanceof EntityNotFoundException) {
            return HttpServletResponse.SC_BAD_REQUEST;
        }
        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }
}
