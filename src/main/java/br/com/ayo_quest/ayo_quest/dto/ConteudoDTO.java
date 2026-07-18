package br.com.ayo_quest.ayo_quest.dto;

import br.com.ayo_quest.ayo_quest.enuns.TipoConteudo;
import lombok.Data;

@Data
public class ConteudoDTO {

    private Long id;

    private TipoConteudo tipo;

    private String valor;

    private String titulo;

}