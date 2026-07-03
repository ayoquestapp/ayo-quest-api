package br.com.ayo_quest.ayo_quest.models;

import br.com.ayo_quest.ayo_quest.enuns.StatusUsuarioTurmaEnum;
import br.com.ayo_quest.ayo_quest.enuns.TipoUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_turma_convite")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TurmaConviteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "turma_id")
    private TurmaEntity turma;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;

    @Column(nullable =false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private StatusUsuarioTurmaEnum status;

    private LocalDateTime expiresAt;

    private LocalDateTime acceptedAt;
}