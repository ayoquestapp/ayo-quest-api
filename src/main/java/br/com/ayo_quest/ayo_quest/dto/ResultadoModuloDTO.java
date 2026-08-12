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
    private Long totalQuestoes;
    private int xpAtual;
    private int xpProximoLevel;
    private int levelAtual;
    private boolean subiuLevel;
    private int coinsGanho;

}