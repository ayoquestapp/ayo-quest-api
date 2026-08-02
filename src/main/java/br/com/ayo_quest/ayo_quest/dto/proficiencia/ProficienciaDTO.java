package br.com.ayo_quest.ayo_quest.dto.proficiencia;

import br.com.ayo_quest.ayo_quest.dto.QuestaoDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProficienciaDTO {

    private Long id;

    private Long nivelId;

    private String nomeNivel;

    private String descricao;

    private Integer notaMinima;

    private Integer totalQuestao;

    private List<Long> questoesIds;
}
