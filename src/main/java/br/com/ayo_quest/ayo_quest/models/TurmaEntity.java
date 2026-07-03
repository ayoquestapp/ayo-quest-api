package br.com.ayo_quest.ayo_quest.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Table(name = "TBL_TURMA")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TurmaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TX_NOME_TURMA")
    private String txNomeTurma;

    @Column(name = "COD_TURMA")
    private String codTurma;

    @Column(name = "QTD_ALUNOS")
    private Long quantidadeAlunos;

    private String periodo;

    private UUID responsavel;

    private String descricao;

    @Column(name = "CREATED_AT")
    private LocalDate createdAt;

    @Column(name = "CREATED_BY")
    private UUID createdBy;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDate updatedAt;

    @Column(name = "ST_TURMA")
    private String stTurma;

    @ManyToMany
    @JoinTable(
            name = "TBL_TURMA_TRILHA",
            joinColumns = @JoinColumn(name = "ID_TURMA"),
            inverseJoinColumns = @JoinColumn(name = "ID_TRILHA")
    )
    private List<TrilhaEntity> trilhas;

}
