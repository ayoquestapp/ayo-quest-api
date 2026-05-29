package br.com.ayo_quest.ayo_quest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuloDTO {
    private Long id;
    private String nome;
    private String descricao;
    private Long cargaHoraria;
    private Integer xpAoConcluir;
    private Long trilhaId;
}
