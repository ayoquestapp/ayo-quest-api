package br.com.ayo_quest.ayo_quest.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DadosProfileDTO {

    private String name;
    private String txNomeExibicao;
    private String email;
    private String localizacao;
    private String bio;

}
