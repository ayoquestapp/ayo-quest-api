package br.com.ayo_quest.ayo_quest.dto.resolver;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SqlResultSetMapping(
        name = "AlternativaResolverMapping",
        classes = @ConstructorResult(
                targetClass = AlternativaResolverDTO.class,
                columns = {

                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "texto", type = String.class),
                        @ColumnResult(name = "correta", type = Boolean.class),
                        @ColumnResult(name = "questao_id", type = Long.class)

                }
        )
)
public class AlternativaResolverDTO {


    @Id
    private Long id;

    private String texto;

    private boolean correta;

    private Long questaoId;


}
