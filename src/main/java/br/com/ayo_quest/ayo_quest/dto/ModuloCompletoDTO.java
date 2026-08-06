package br.com.ayo_quest.ayo_quest.dto;

import br.com.ayo_quest.ayo_quest.dto.resolver.QuestaoResolverDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModuloCompletoDTO {

    private Long id;
    private String nome;
    private String descricao;
    private Long cargaHoraria;
    private Long xpAoConcluir;
    private Long trilhaId;
    private String nomeTrilha;

    private List<ConteudoDTO> conteudos;

    private List<QuestaoResolverDTO> questoes;
}
