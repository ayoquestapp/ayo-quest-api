package br.com.ayo_quest.ayo_quest.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModuloCadastroDTO {

    private String nome;

    private String descricao;

    private Long cargaHoraria;

    private Long xpAoConcluir;

    private Long notaMinima;

    private Long trilhaId;

    private Long nivelId;

    private List<ConteudoDTO> conteudos;

    private List<QuestaoDTO> questoes;

//    private Long tempoPorQuestao;


}
