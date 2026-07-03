package br.com.ayo_quest.ayo_quest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrilhaDTO {

    private Long id;
    private String nome;
    private String code;
    private String descricao;
    private Integer quantidadeModulos;
    private String imagem;


}