package br.com.ayo_quest.ayo_quest.dto;

import lombok.Data;

@Data
public class TrilhaCreateDTO {
    private String nome;
    private String code;
    private String descricao;
    private String tag;
    private String imagem;
}
