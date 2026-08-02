package br.com.ayo_quest.ayo_quest.dto.resolver;


import br.com.ayo_quest.ayo_quest.dto.ConteudoDTO;
import br.com.ayo_quest.ayo_quest.dto.QuestaoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SqlResultSetMapping(
        name = "ModuloResolverDTOMapping",
        classes = @ConstructorResult(
                targetClass = ModuloResolverDTO.class,
                columns = {

                        @ColumnResult(
                                name = "id",
                                type = Long.class
                        ),

                        @ColumnResult(
                                name = "nome",
                                type = String.class
                        ),

                        @ColumnResult(
                                name = "descricao",
                                type = String.class
                        ),

                        @ColumnResult(
                                name = "carga_horaria",
                                type = Long.class
                        ),

                        @ColumnResult(
                                name = "xp_ao_concluir",
                                type = Long.class
                        )
                }
        )
)
public class ModuloResolverDTO {

    @Id
    private Long id;

    private String nome;

    private String descricao;

    private Long cargaHoraria;

    private Long xpAoConcluir;

    @Transient
    private List<ConteudoDTO> conteudos;

    @Transient
    private List<QuestaoResolverDTO> questoes;


    public ModuloResolverDTO(
            Long id,
            String nome,
            String descricao,
            Long cargaHoraria,
            Long xpAoConcluir
    ) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.cargaHoraria = cargaHoraria;
        this.xpAoConcluir = xpAoConcluir;
    }
}