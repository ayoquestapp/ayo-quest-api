package br.com.ayo_quest.ayo_quest.models;

import br.com.ayo_quest.ayo_quest.enuns.StatusProficiencia;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_usuario_proficiencia")
@Data
public class UsuarioProficienciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_proficiencia")
    private Long id;

    @ManyToOne
    private ProfileEntity usuario;

    @ManyToOne
    private ProficienciaEntity proficiencia;

    @Column(name = "nota")
    private Double nota;

    @Column(name = "tentativas")
    private Integer tentativas;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusProficiencia status;

    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;
}