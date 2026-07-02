package br.com.ayo_quest.ayo_quest.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class TurmaCadastroDTO {
    private String txNomeTurma;
    private String codTurma;
    private String descricao;
    private List<AlunoConviteDTO> alunos;
    private UUID responsavel;
    private List<Long> trilhaIds;
    private String periodo;

}
