package br.com.ayo_quest.ayo_quest.security;

import br.com.ayo_quest.ayo_quest.models.UsuarioEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.UUID;


@Service
public class JWTService {


    private final SecretKey chave;


    public JWTService(
            @Value("${jwt.secret}") String secret
    ){

        this.chave =
                Keys.hmacShaKeyFor(
                        secret.getBytes(StandardCharsets.UTF_8)
                );
    }



    public String gerarToken(
            UsuarioEntity usuario
    ){

        return Jwts.builder()

                // identidade principal
                .subject(
                        usuario.getId().toString()
                )

                // informações adicionais
                .claim(
                        "email",
                        usuario.getEmail()
                )

                .claim(
                        "role",
                        usuario.getRole().name()
                )


                .issuedAt(
                        new Date()
                )


                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 86400000
                        )
                )


                .signWith(chave)

                .compact();
    }





    public UUID getUserId(
            String token
    ){

        String id =

                Jwts.parser()

                        .verifyWith(chave)

                        .build()

                        .parseSignedClaims(token)

                        .getPayload()

                        .getSubject();



        return UUID.fromString(id);

    }





    public String getEmail(
            String token
    ){

        Claims claims =

                Jwts.parser()

                        .verifyWith(chave)

                        .build()

                        .parseSignedClaims(token)

                        .getPayload();



        return claims.get(
                "email",
                String.class
        );

    }





    public String getRole(
            String token
    ){

        Claims claims =

                Jwts.parser()

                        .verifyWith(chave)

                        .build()

                        .parseSignedClaims(token)

                        .getPayload();



        return claims.get(
                "role",
                String.class
        );

    }




    public boolean validarToken(
            String token
    ){

        try {

            Jwts.parser()

                    .verifyWith(chave)

                    .build()

                    .parseSignedClaims(token);


            return true;


        }catch(Exception e){

            return false;

        }

    }

}