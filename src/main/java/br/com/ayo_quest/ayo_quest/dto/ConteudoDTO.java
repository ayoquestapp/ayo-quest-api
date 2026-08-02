package br.com.ayo_quest.ayo_quest.dto;

import br.com.ayo_quest.ayo_quest.enuns.TipoConteudo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SqlResultSetMapping(
        name = "ConteudoResolverMapping",
        classes = @ConstructorResult(
                targetClass = ConteudoDTO.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "tipo", type = String.class),
                        @ColumnResult(name = "valor", type = String.class),
                        @ColumnResult(name = "titulo", type = String.class)
                }
        )
)
public class ConteudoDTO {

    @Id
    private Long id;

    private TipoConteudo tipo;

    private String valor;

    private String titulo;

    public ConteudoDTO(Long id, String tipo, String valor, String titulo) {
        this.id = id;
        this.tipo = TipoConteudo.valueOf(tipo);
        this.valor = valor;
        this.titulo = titulo;
    }

}


