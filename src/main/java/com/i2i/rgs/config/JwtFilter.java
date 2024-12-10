package com.i2i.rgs.config;

import com.i2i.rgs.helper.UnAuthorizedException;
import com.i2i.rgs.model.User;
import com.i2i.rgs.util.JwtUtil;
import com.i2i.rgs.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * JwtFilter is a custom filter that processes JWT authentication for each request.
 * It extends OncePerRequestFilter to ensure it is executed once per request.
 */
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private ApplicationContext context;

    private final HandlerExceptionResolver handlerExceptionResolver;

    /**
     * Constructor to initialize the JwtFilter with a HandlerExceptionResolver.
     *
     * @param handlerExceptionResolver the resolver to handle exceptions
     */
    public JwtFilter(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver
                             handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    /**
     * <p>
     * Validates the authorization header and extracts the token.
     * </p>
     *
     * @param authHeader the authorization header containing the token
     * @return the extracted token.
     * @throws UnAuthorizedException if the token is invalid or missing
     */
    private String validateAndExtractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnAuthorizedException("Invalid token");
        }
        return authHeader.substring(7);
    }

    /**
     * Filters incoming HTTP requests to handle authentication and authorization.
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain
     * @throws IOException      if an input or output error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException,
            ServletException {
        if (request.getRequestURI().contains("/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token;
        String username;

        try {
            token = validateAndExtractToken(authHeader);
            username = JwtUtil.extractUserName(token);
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
            }
            User userDetails = (User) context.getBean(MyUserDetailsService.class)
                    .loadUserByUsername(username);
            request.setAttribute("id", userDetails.getId());
            JwtUtil.validateToken(token, userDetails);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}