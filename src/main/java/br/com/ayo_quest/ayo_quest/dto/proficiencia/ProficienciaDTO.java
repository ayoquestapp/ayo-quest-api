package br.com.ayo_quest.ayo_quest.dto.proficiencia;

import br.com.ayo_quest.ayo_quest.dto.QuestaoDTO;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProficienciaDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long nivelId;

    private String nomeNivel;

    private String descricao;

    private Integer notaMinima;

    private Integer totalQuestao;

    private List<Long> questoesIds;
}
