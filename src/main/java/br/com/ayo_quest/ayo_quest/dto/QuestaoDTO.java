package br.com.ayo_quest.ayo_quest.dto;

import br.com.ayo_quest.ayo_quest.enuns.TipoQuestao;
import lombok.Data;

import java.util.List;

@Data
public class QuestaoDTO {

    private Long id;

    private TipoQuestao tipo;

    private String tipoDescricao;

    private String enunciado;

    private Integer xp;

    private List<AlternativaDTO> alternativas;

}