package br.com.ayo_quest.ayo_quest.dto;

import br.com.ayo_quest.ayo_quest.enuns.TipoQuestao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@SqlResultSetMapping(name = "QuestaoDTOMapping", classes = @ConstructorResult(targetClass = QuestaoDTO.class,
        columns = {
                @ColumnResult(name = "id", type = Long.class),
                @ColumnResult(name = "tipo", type = String.class),
                @ColumnResult(name = "enunciado", type = String.class),
                @ColumnResult(name = "xp", type = Integer.class),
                @ColumnResult(name = "tempo_por_questao", type = Integer.class)
        })
)
public class QuestaoDTO {
    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    private TipoQuestao tipo;
    private String tipoDescricao;
    private String enunciado;
    private Integer xp;
    @Column(name = "tempo_por_questao")
    private Integer tempoPorQuestao;
    @Transient
    private List<AlternativaDTO> alternativas;

    public QuestaoDTO(Long id, String tipo, String enunciado, Integer xp , Integer tempoPorQuestao) {
        this.id = id;
        this.tipo = TipoQuestao.valueOf(tipo);
        this.enunciado = enunciado;
        this.xp = xp;
        this.tempoPorQuestao = tempoPorQuestao;
    }
}