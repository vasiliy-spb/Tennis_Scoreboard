package dev.chearcode.filter;

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

@WebFilter("/*")
public class ExceptionHandler extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        try {
            super.doFilter(req, res, chain);
        } catch (Exception e) {
            handleException(e, req, res);
        }
    }

    private void handleException(Exception e, HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        int statusCode = getStatusCode(e);
        req.setAttribute("errorMessage", getErrorMessage(e, statusCode));
        req.setAttribute("statusCode", statusCode);

        if (e instanceof ValidationException) {
            String path = req.getServletPath() + ".jsp";
            forwardTo(req, path, res);
            return;
        }

        if (statusCode == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            forwardTo(req, "/WEB-INF/jsp/error-500.jsp", res);
            return;
        }

        forwardTo(req, "/WEB-INF/jsp/error.jsp", res);
    }

    private int getStatusCode(Exception e) {
        if (e instanceof ValidationException) {
            return HttpServletResponse.SC_BAD_REQUEST;
        }
        if (e instanceof EntityNotFoundException) {
            return HttpServletResponse.SC_NOT_FOUND;
        }
        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }

    private String getErrorMessage(Exception e, int statusCode) {
        if (statusCode == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            return "Internal server error. Try again latter.";
        }
        return e.getMessage();
    }

    private void forwardTo(HttpServletRequest req, String path, HttpServletResponse res)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(path);
        dispatcher.forward(req, res);
    }
}
