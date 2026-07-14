package br.com.ayo_quest.ayo_quest.models;

import br.com.ayo_quest.ayo_quest.enuns.PrioridadeNotificacao;
import br.com.ayo_quest.ayo_quest.enuns.TipoNotificacao;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tbl_notificacao")
public class NotificacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensagem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoNotificacao tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioridadeNotificacao prioridade = PrioridadeNotificacao.INFO;

    @Column(length = 50)
    private String icone;

    @Column(length = 255)
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origem_usuario")
    private ProfileEntity origemUsuario;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

}