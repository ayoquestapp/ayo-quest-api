package br.com.ayo_quest.ayo_quest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrilhaUpdateDTO {

    private String nome;

    private String code;

    private String descricao;

    private String imagem;

}
