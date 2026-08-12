package br.com.ayo_quest.ayo_quest.dto;

import lombok.Data;

import java.util.List;

@Data
public class ModuloResponseDTO {

    private Long id;

    private String nome;

    private String descricao;

    private Long cargaHoraria;

    private Long xpAoConcluir;

    private Long tempoPorQuestao;

    private Long notaMinima;

    private Long nivelId;

    private Long trilhaId;

    private List<ConteudoDTO> conteudos;

    private List<QuestaoDTO> questoes;
}
