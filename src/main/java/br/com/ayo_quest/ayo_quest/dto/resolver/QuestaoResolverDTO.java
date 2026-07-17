package br.com.ayo_quest.ayo_quest.dto.resolver;

import br.com.ayo_quest.ayo_quest.enuns.TipoQuestao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestaoResolverDTO {

    private Long id;

    private String enunciado;

    private TipoQuestao tipo;

    private Integer xp;

    private List<AlternativaResolverDTO> alternativas;

}
