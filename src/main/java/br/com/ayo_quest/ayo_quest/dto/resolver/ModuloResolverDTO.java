package br.com.ayo_quest.ayo_quest.dto.resolver;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuloResolverDTO {

    private Long id;

    private String nome;

    private String descricao;

    private Long cargaHoraria;

    private Long xpAoConcluir;

    private List<ConteudoResolverDTO> conteudos;

    private List<QuestaoResolverDTO> questoes;

}
