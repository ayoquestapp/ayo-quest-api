package br.com.ayo_quest.ayo_quest.dto.tentativaModulo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoRespostaDTO {

    private Integer acertos;

    private Integer erros;

    private Integer xpGanho;

    private Double nota;

}

