package br.com.ayo_quest.ayo_quest.dto;

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
@SqlResultSetMapping(
        name = "ModuloDTOMapping",
        entities = @EntityResult(
                entityClass = ModuloDTO.class,
                fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "nome", column = "nome"),
                        @FieldResult(name = "descricao", column = "descricao"),
                        @FieldResult(name = "notaMinima", column = "nota_minima"),
                        @FieldResult(name = "cargaHoraria", column = "carga_horaria"),
                        @FieldResult(name = "xpAoConcluir", column = "xp_ao_concluir"),
                        @FieldResult(name = "trilhaId", column = "trilha_id"),
                        @FieldResult(name = "nivelId" , column = "nivel_id"),
                        @FieldResult(name = "nomeNivel" , column = "nome_nivel"),
                        @FieldResult(name = "nomeTrilha", column = "nome_trilha")
                }
        )
)
@Entity
public class ModuloDTO {
    @Id
    private Long id;
    private String nome;
    private String descricao;
    private Long notaMinima;
    private Long cargaHoraria;
    private Long xpAoConcluir;
    private Long trilhaId;
    private Long nivelId;
    private String nomeNivel;
    private String nomeTrilha;
    @Transient
    private List<ConteudoDTO> conteudos;

    @Transient
    private List<QuestaoDTO> questoes;

}
