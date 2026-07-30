package br.com.ayo_quest.ayo_quest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModuloDTO {
    private Long id;
    private String nome;
    private String descricao;
    private Long cargaHoraria;
    private Long xpAoConcluir;
    private TrilhaResumoDTO trilha;
    private List<ConteudoDTO> conteudos;
    private List<QuestaoDTO> questoes;
}
