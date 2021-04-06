package com.app.expd.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtProvider jwtProvider;

    @Qualifier("userDetailServiceImpl")
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    @Transactional(readOnly = true)
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
       String jwt = getJwtFromRequest(httpServletRequest);
       if(StringUtils.hasText(jwt) && jwtProvider.validateJWTtoken(jwt)){
           String username = jwtProvider.getUserNameFromToken(jwt);
           UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
           UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                   userDetails.getUsername(),null,userDetails.getAuthorities()
           );
           authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
           SecurityContextHolder.getContext().setAuthentication(authenticationToken);

       }
       filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    public String  getJwtFromRequest(HttpServletRequest httpServletRequest) {

        String bearerToken = httpServletRequest.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return bearerToken;
    }
}
