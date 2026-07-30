package br.com.ayo_quest.ayo_quest.dto;

import br.com.ayo_quest.ayo_quest.enuns.TipoConteudo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConteudoDTO {

    private Long id;

    private TipoConteudo tipo;

    private String valor;

    private String titulo;

}