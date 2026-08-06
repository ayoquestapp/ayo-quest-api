package br.com.ayo_quest.ayo_quest.dto.auth;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String senha;
}
