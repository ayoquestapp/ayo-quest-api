package br.com.ayo_quest.ayo_quest.dto;

import br.com.ayo_quest.ayo_quest.enuns.TiposPeriodos;
import lombok.AllArgsConstructor;

import lombok.Data;


@Data
@AllArgsConstructor
public class PeriodoDTO {

    private TiposPeriodos periodos;

}