package br.com.ayo_quest.ayo_quest.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;

    private UUID id;

    private String nome;

    private String email;

    private String role;

}