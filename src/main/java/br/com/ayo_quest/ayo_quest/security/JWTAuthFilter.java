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
import java.util.UUID;

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



        String path =
                request.getServletPath();



        if(
                path.startsWith("/auth")
                        ||
                        path.startsWith("/api/auth")
        ){

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }



        String header =
                request.getHeader(
                        "Authorization"
                );



        if(
                header == null
                        ||
                        !header.startsWith("Bearer ")
        ){

            filterChain.doFilter(
                    request,
                    response
            );

            return;

        }



        String token =
                header.substring(7);



        try {


            UUID userId =
                    jwtService.getUserId(token);



            String role =
                    jwtService.getRole(token);



            System.out.println(
                    "USER UUID JWT: "
                            + userId
            );


            System.out.println(
                    "ROLE JWT: "
                            + role
            );



            UsernamePasswordAuthenticationToken authentication =


                    new UsernamePasswordAuthenticationToken(

                            userId.toString(),

                            null,


                            List.of(

                                    new SimpleGrantedAuthority(

                                            "ROLE_" + role

                                    )

                            )

                    );




            SecurityContextHolder

                    .getContext()

                    .setAuthentication(authentication);



        }catch(Exception e){


            SecurityContextHolder

                    .clearContext();

        }



        filterChain.doFilter(
                request,
                response
        );

    }


}