package com.ecommerce.orderservice.filter;

import com.ecommerce.orderservice.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private  final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String  header=  request.getHeader("Authorization");

        if(header==null || !header.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        String jwt=header.substring(7);
        String userName= jwtService.extractUserName(jwt);
        if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null){

            if(jwtService.isTokenValid(jwt)){
                String role= jwtService.extractRole(jwt);
                List<GrantedAuthority> authorities=List.of(new
                        SimpleGrantedAuthority(role));
                UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(
                        userName,null,authorities
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }
        filterChain.doFilter(request,response);


    }
}
