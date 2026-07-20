package br.com.ayo_quest.ayo_quest.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultadoModuloDTO {
    private int acertos;
    private int erros;
    private int xpGanho;
    private double nota;
    private boolean aprovado;
}