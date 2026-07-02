package br.com.ayo_quest.ayo_quest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TurmaDTO {
    private Long id;
    private String codTurma;
    private String txNomeTurma;
    private Long quantidadeAlunos;
    private String periodo;
    private ResponsavelDTO responsavel;
    private String descricao;
    private LocalDate created_at;
    private UUID created_by;
    private LocalDate updated_at;
    private String stTurma;

}
