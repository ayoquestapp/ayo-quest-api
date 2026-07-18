package br.com.ayo_quest.ayo_quest.dto.tentativaModulo;

import lombok.Data;

import java.util.List;

@Data
public class RespostaQuestaoDTO {

    private Long questaoId;

    private List<Long> alternativas;

    private String respostaTexto;

}
