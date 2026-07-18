package br.com.ayo_quest.ayo_quest.dto.tentativaModulo;

import lombok.Data;

import java.util.List;

@Data
public class EnviarRespostaDTO {

    private Long tentativaId;

    private List<RespostaQuestaoDTO> respostas;

}