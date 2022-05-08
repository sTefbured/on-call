package com.stefbured.oncallserver.jwt;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stefbured.oncallserver.config.JwtConfiguration;
import com.stefbured.oncallserver.exception.authentication.AuthenticationAttemptFailedException;
import com.stefbured.oncallserver.model.dto.user.UserDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import static com.stefbured.oncallserver.jwt.JwtConstants.AUTH_COOKIE_NAME;

public class JwtUsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {
    private static final RequestMatcher AUTH_REQUEST_MATCHER = new AntPathRequestMatcher("/api/v1/user/login", "POST");

    private final AuthenticationManager authenticationManager;
    private final JwtConfiguration jwtConfiguration;

    public JwtUsernamePasswordAuthFilter(AuthenticationManager authenticationManager,
                                         JwtConfiguration jwtConfiguration) {
        this.authenticationManager = authenticationManager;
        this.jwtConfiguration = jwtConfiguration;
        setRequiresAuthenticationRequestMatcher(AUTH_REQUEST_MATCHER);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            var authRequest = new ObjectMapper().readValue(request.getInputStream(), UserDTO.class);
            var authentication = new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(),
                    authRequest.getPassword()
            );
            return authenticationManager.authenticate(authentication);
        } catch (IOException exception) {
            throw new AuthenticationAttemptFailedException("Authentication attempt failed.", exception);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        var token = JWT.create()
                .withSubject(authResult.getName())
                .withIssuedAt(new Date())
                .withExpiresAt(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfiguration.getTokenExpirationAfterDays())))
                .sign(jwtConfiguration.getAlgorithm());
        response.addHeader(jwtConfiguration.getAuthorizationHeader(), jwtConfiguration.getTokenPrefix() + ' ' + token);
        var cookie = new Cookie(AUTH_COOKIE_NAME, token);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }
}
