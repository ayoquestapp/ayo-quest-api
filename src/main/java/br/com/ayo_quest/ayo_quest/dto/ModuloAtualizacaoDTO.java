package br.com.ayo_quest.ayo_quest.dto;

import lombok.Data;

import java.util.List;

@Data
public class ModuloAtualizacaoDTO {

    private String nome;
    private String descricao;
    private Long cargaHoraria;
    private Long xpAoConcluir;
    private Long trilhaId;
    private Long tempoPorQuestao;
    private List<ConteudoDTO> conteudos;
    private List<QuestaoDTO> questoes;

}
