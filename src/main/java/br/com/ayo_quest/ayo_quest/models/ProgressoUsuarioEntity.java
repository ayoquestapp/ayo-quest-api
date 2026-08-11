package br.com.ayo_quest.ayo_quest.models;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_progresso_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgressoUsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(
            name = "profile_id",
            nullable = false,
            unique = true
    )
    private ProfileEntity profile;


    @Column(name = "xp_total", nullable = false)
    @Builder.Default
    private Integer xpTotal = 0;


    @ManyToOne
    @JoinColumn(
            name = "level_id",
            nullable = false
    )
    private LevelEntity level;


    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

    }


    @PreUpdate
    public void preUpdate() {

        updatedAt = LocalDateTime.now();

    }

}