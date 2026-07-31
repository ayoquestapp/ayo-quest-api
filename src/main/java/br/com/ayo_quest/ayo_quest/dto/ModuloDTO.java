package br.com.ayo_quest.ayo_quest.dto;

import jakarta.persistence.Id;
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
    @Id
    private Long id;
    private String nome;
    private String descricao;
    private Long cargaHoraria;
    private Long xpAoConcluir;
    private Long trilhaId;
    private String nomeTrilha;
    private List<Long> conteudos_ids;
    private List<Long> questoes_ids;
    private Long tempoMaximo;
}
