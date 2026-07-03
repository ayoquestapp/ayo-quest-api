package br.com.ayo_quest.ayo_quest.dto;

import lombok.Data;

import java.util.List;

@Data
public class ListarModuloDTO {
    private Long id;
    private String nome;
    private String descricao;

    private List<ListarConteudoDTO> conteudos;
    private List<ListarQuestaoDTO> questoes;


}
