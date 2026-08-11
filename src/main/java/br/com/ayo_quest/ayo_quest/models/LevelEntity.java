package br.com.ayo_quest.ayo_quest.models;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_level", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LevelEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private Integer nivel;


    @Column(name = "xp_necessario", nullable = false)
    private Integer xpNecessario;


    @Column(name = "recompensa_coins", nullable = false)
    @Builder.Default
    private Integer recompensaCoins = 0;


    @Column(length = 100)
    private String titulo;


    @Column(length = 255)
    private String icone;


    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @PrePersist
    public void prePersist(){

        if(createdAt == null){
            createdAt = LocalDateTime.now();
        }

    }

}
