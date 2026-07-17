package br.com.ayo_quest.ayo_quest.models;

import br.com.ayo_quest.ayo_quest.enuns.TipoQuestao;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TBL_QUESTAO")
public class QuestaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String enunciado;

    @Enumerated(EnumType.STRING)
    private TipoQuestao tipo;

    private Integer xp;

    @ManyToOne
    @JoinColumn(name = "modulo_id")
    @JsonIgnore
    private ModuloEntity modulo;

    @OneToMany(
            mappedBy = "questao",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AlternativaEntity> alternativas = new ArrayList<>();
}


