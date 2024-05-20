package org.example.backend.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Autowired
    private APIConfig apiConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();

        // Allow OPTIONS requests and unsecured paths to proceed without authentication
        if (HttpMethod.OPTIONS.matches(request.getMethod()) ||
                apiConfig.SECURED_PATHS.stream().noneMatch(servletPath::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the token from the Authorization header
        String encryptedToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (encryptedToken == null || !encryptedToken.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token provided. You need to logon first.");
            return;
        }

        String token = encryptedToken.substring(7); // Remove "Bearer " prefix

        try {
            // Decode the token
            JWToken jwToken = JWToken.decode(token, apiConfig.getIssuer(), apiConfig.getPassphrase());

            // If needed, set attributes in the request for later use
            request.setAttribute("JWTokenInfo", jwToken);

            // Proceed with the filter chain
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
        } catch (MalformedJwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the token");
        }
    }
}
