package br.com.ayo_quest.ayo_quest.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(
        name = "tbl_usuario_notificacao",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"notificacao_id", "usuario_id"})
        }
)
public class UsuarioNotificacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notificacao_id", nullable = false)
    private NotificacaoEntity notificacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private ProfileEntity usuario;

    @Column(nullable = false)
    private Boolean lida = false;

    @Column(name = "data_leitura")
    private LocalDateTime dataLeitura;

    @Column(nullable = false)
    private Boolean arquivada = false;

}
