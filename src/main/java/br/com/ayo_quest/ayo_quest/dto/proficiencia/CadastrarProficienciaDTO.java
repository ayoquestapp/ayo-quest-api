package br.com.ayo_quest.ayo_quest.dto.proficiencia;

import br.com.ayo_quest.ayo_quest.dto.QuestaoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CadastrarProficienciaDTO {

    private Long nivelId;

    private String nome;

    private String descricao;

    private Integer notaMinima;

    private Integer totalQuestao;

    private List<QuestaoDTO> questoes;
}
