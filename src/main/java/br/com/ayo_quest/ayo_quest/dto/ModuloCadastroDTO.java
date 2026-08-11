package br.com.ayo_quest.ayo_quest.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class ModuloCadastroDTO {

    private String nome;

    private String descricao;

    private Long cargaHoraria;

    private Long xpAoConcluir;

    private Long notaMinima;

    private Long trilhaId;

    private List<ConteudoDTO> conteudos;

    private List<QuestaoDTO> questoes;

//    private Long tempoPorQuestao;


}
