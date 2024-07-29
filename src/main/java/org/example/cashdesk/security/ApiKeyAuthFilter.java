package org.example.cashdesk.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class ApiKeyAuthFilter implements Filter {

    @Value("${api.secret}")
    private String apiKey;

    private static final String API_KEY_HEADER_NAME = "FIB-X-AUTH";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestApiKey = httpRequest.getHeader(API_KEY_HEADER_NAME);

        if (apiKey.equals(requestApiKey)) {
            chain.doFilter(request, response);
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
            PrintWriter out = httpResponse.getWriter();
            out.write("{\"error\":\"Invalid API key\"}");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}
