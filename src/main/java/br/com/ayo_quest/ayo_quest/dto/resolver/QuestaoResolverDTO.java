package br.com.ayo_quest.ayo_quest.dto.resolver;

import br.com.ayo_quest.ayo_quest.enuns.TipoQuestao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SqlResultSetMapping(
        name = "QuestaoResolverMapping",
        classes = @ConstructorResult(
                targetClass = QuestaoResolverDTO.class,
                columns = {

                        @ColumnResult(
                                name = "id",
                                type = Long.class
                        ),

                        @ColumnResult(
                                name = "enunciado",
                                type = String.class
                        ),

                        @ColumnResult(
                                name = "tipo",
                                type = String.class
                        ),

                        @ColumnResult(
                                name = "xp",
                                type = Integer.class
                        )
                }
        )
)
public class QuestaoResolverDTO {

    @Id
    private Long id;

    private String enunciado;

    private TipoQuestao tipo;

    private Integer xp;

    @Transient
    private List<AlternativaResolverDTO> alternativas = new ArrayList<>();


    public QuestaoResolverDTO(
            Long id,
            String enunciado,
            String tipo,
            Integer xp
    ) {
        this.id = id;
        this.enunciado = enunciado;
        this.tipo = TipoQuestao.valueOf(tipo);
        this.xp = xp;
    }
}