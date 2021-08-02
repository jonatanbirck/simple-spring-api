package com.jonatan.simplespringapi.domain.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jonatan.simplespringapi.domain.entity.User;
import com.jonatan.simplespringapi.domain.service.UserService;
import com.jonatan.simplespringapi.domain.entity.Role;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public abstract class TokenManager {
    
    private static final Algorithm ALGORITHM = Algorithm.HMAC256("secret".getBytes());
    private static final JWTVerifier VERIFIER = JWT.require(ALGORITHM).build();

    private static final int MIN_ACCESS_EXPIRES = 30;
    private static final int MIN_REFRESH_EXPIRES = 60;

    private static Date newAccessExpiresAt() {
        return new Date(System.currentTimeMillis() + (MIN_ACCESS_EXPIRES * 60 * 1000));
    }

    private static Date newRefreshExpiresAt() {
        return new Date(System.currentTimeMillis() + (MIN_REFRESH_EXPIRES * 60 * 1000));
    }    

    public static Map<String,String> createTokens(org.springframework.security.core.userdetails.User user, HttpServletRequest request, HttpServletResponse response) {
        String accessToken = JWT.create()
            .withSubject(user.getUsername())
            .withExpiresAt(newAccessExpiresAt())
            .withIssuer(request.getRequestURL().toString())
            .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
            .sign(ALGORITHM);

        String refreshToken = JWT.create()
            .withSubject(user.getUsername())
            .withExpiresAt(newRefreshExpiresAt())
            .withIssuer(request.getRequestURL().toString())
            .sign(ALGORITHM);            

        Map<String,String> tokens = new HashMap<>();
        tokens.put("acess_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        
        return tokens;
    }

    public static UsernamePasswordAuthenticationToken authenticateToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String authenticationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            
        if( authenticationHeader != null && authenticationHeader.startsWith("Bearer ")) {
            String token = authenticationHeader.substring("Bearer ".length());

            DecodedJWT decodeJWT = VERIFIER.verify(token);

            String login = decodeJWT.getSubject();
            String[] roles = decodeJWT.getClaim("roles").asArray(String.class);

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            
            Arrays.stream(roles).forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role));
            });
            
            return new UsernamePasswordAuthenticationToken(login, null, authorities);
        }

        return null;
    }

    public static Map<String,String> refreshToken(HttpServletRequest request, HttpServletResponse response, UserService userService) throws Exception {
        String authenticationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            
        if( authenticationHeader != null && authenticationHeader.startsWith("Bearer ")) {
            String refreshToken = authenticationHeader.substring("Bearer ".length());
        
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT decodeJWT = verifier.verify(refreshToken);

            String login = decodeJWT.getSubject();

            Optional<User> user = userService.getUser(login);

            String accessToken = JWT.create()
                .withSubject(user.get().getLogin())
                .withExpiresAt(newAccessExpiresAt())
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.get().getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(algorithm);
        
            Map<String,String> tokens = new HashMap<>();
            tokens.put("acess_token", accessToken);
            tokens.put("refresh_token", refreshToken);

            return tokens;
        }

        return null;
    }

}
