package br.com.ayo_quest.ayo_quest.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {


    private final JWTService jwtService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {


        String path = request.getServletPath();


        if (
                path.startsWith("/auth") ||
                        path.startsWith("/api/auth") ||
                        path.startsWith("/api/storage")
        ) {
            filterChain.doFilter(request, response);
            return;
        }


        String authHeader = request.getHeader("Authorization");


        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }


        String token = authHeader.substring(7);


        try {

            String email = jwtService.getEmail(token);

            String role = jwtService.getRole(token);

            System.out.println("EMAIL JWT: " + email);
            System.out.println("ROLE JWT: " + role);


            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            List.of(
                                    new SimpleGrantedAuthority(
                                            "ROLE_" + role
                                    )
                            )
                    );


            System.out.println(
                    "AUTHORITIES: " + authentication.getAuthorities()
            );


            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);


        } catch(Exception e){
            e.printStackTrace();
            SecurityContextHolder.clearContext();
        }


        filterChain.doFilter(request,response);
    }

}