package br.com.ayo_quest.ayo_quest.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import java.util.UUID;

@SqlResultSetMapping(
        name = "ResponsavelDTOMapping",
        classes = {
                @ConstructorResult(
                        targetClass = ResponsavelDTO.class,
                        columns = {
                                @ColumnResult(name = "id" , type = UUID.class),
                                @ColumnResult(name = "nome" , type = String.class),
                                @ColumnResult(name = "email" , type = String.class)
                        }
                )
        }
)
@Data
@Entity
public class ResponsavelDTO {
    @Id
    private UUID id;

    private String nome;

    private String email;
}
