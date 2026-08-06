package br.com.ayo_quest.ayo_quest.dto;

import br.com.ayo_quest.ayo_quest.enuns.TipoUsuario;
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
    private TipoUsuario role;
    private String bio;
    private Integer level;
    private Long xp;

}
