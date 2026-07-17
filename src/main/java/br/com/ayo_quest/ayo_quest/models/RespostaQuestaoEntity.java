package br.com.ayo_quest.ayo_quest.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TBL_RESPOSTA_QUESTAO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespostaQuestaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TentativaModuloEntity tentativa;

    @ManyToOne
    private QuestaoEntity questao;

    @Column(columnDefinition = "TEXT")
    private String respostaTexto;

    private Boolean correta;

    private Integer xpObtido;

    private Integer nota;
}
