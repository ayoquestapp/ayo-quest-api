package br.com.ayo_quest.ayo_quest.dto;

import lombok.Data;

import java.util.List;

@Data
public class ModuloCadastroDTO {

    private String nome;

    private String descricao;

    private Long cargaHoraria;

    private Long xpAoConcluir;

    private Long trilhaId;

    private List<Long> conteudos_ids;

    private List<Long> questoes_ids;

    private Long tempoMaximo;


}
