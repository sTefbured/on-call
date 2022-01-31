package com.stefbured.oncallserver.jwt;

import com.auth0.jwt.JWT;
import com.stefbured.oncallserver.config.JwtConfiguration;
import lombok.NonNull;
import org.apache.logging.log4j.util.Strings;
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
    private final JwtConfiguration jwtConfiguration;

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
        var tokenVerifier = JWT.require(jwtConfiguration.getAlgorithm()).build();
        var decodedToken = tokenVerifier.verify(token);
        var username = decodedToken.getSubject();
        var authorities = decodedToken.getClaim(AUTHORITIES_CLAIM_NAME).asArray(String.class);
        var grantedAuthorities = Arrays.stream(authorities).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        var authentication = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
