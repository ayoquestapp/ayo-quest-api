package br.com.ayo_quest.ayo_quest.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TBL_RESPOSTA_ALTERNATIVA")
@Data
public class RespostaAlternativaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "resposta_questao_id")
    private RespostaQuestaoEntity respostaQuestao;


    @ManyToOne
    @JoinColumn(name = "alternativa_id")
    private AlternativaEntity alternativa;

}

