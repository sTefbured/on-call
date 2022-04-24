package com.stefbured.oncallserver.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.stefbured.oncallserver.config.JwtConfiguration;
import com.stefbured.oncallserver.exception.user.UserNotFoundException;
import com.stefbured.oncallserver.service.UserService;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.stefbured.oncallserver.jwt.JwtConstants.AUTHORITIES_CLAIM_NAME;

public class JwtTokenVerifierFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LogManager.getLogger(JwtTokenVerifierFilter.class);

    private final JwtConfiguration jwtConfiguration;

    private UserService userService;

    public JwtTokenVerifierFilter(JwtConfiguration jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        var authorizationHeader = request.getHeader(jwtConfiguration.getAuthorizationHeader());
        if (Strings.isEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfiguration.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }
        var token = authorizationHeader.replace(jwtConfiguration.getTokenPrefix() + ' ', "");
        var algorithm = jwtConfiguration.getAlgorithm();
        verifyToken(token, algorithm);
        filterChain.doFilter(request, response);
    }

    private void verifyToken(String token, Algorithm algorithm) {
        var tokenVerifier = JWT.require(algorithm).build();
        try {
            var decodedToken = tokenVerifier.verify(token);
            var username = decodedToken.getSubject();
            var queriedUser = userService.getUserByUsername(username);
            if (Boolean.TRUE.equals(queriedUser.isBanned())) {
                return;
            }
            var authorities = decodedToken.getClaim(AUTHORITIES_CLAIM_NAME).asArray(String.class);
            var grantedAuthorities = Arrays.stream(authorities)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
            var authentication = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
            authentication.setDetails(queriedUser.getId());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            LOGGER.debug("Token verification success: authentication={}", authentication);
        } catch (SignatureVerificationException | UserNotFoundException exception) {
            LOGGER.debug("Failed to verify JWT token: exception={}", exception.getMessage());
        }
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
