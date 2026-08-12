package br.com.ayo_quest.ayo_quest.models;

import br.com.ayo_quest.ayo_quest.enuns.StatusTentativa;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TBL_TENTATIVA_MODULO")
public class TentativaModuloEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private ProfileEntity profile;

    @ManyToOne
    private ModuloEntity modulo;

    private LocalDateTime inicio;

    private LocalDateTime fim;

    private double nota;

    private Integer xpGanho;

    private boolean aprovado;

    @Enumerated(EnumType.STRING)
    private StatusTentativa status;
}
