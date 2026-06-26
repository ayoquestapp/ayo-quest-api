package br.com.ayo_quest.ayo_quest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class TurmaDTO {
    private String codTurma;
    private String txNomeTurma;
    private Long quantidadeAlunos;
    private String periodo;
    private UUID responsavel;
    private String descricao;
    private LocalDate created_at;
    private UUID created_by;
    private LocalDate updated_at;
    private String stTurma;
}
