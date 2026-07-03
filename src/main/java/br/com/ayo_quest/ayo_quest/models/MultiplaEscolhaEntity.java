package br.com.ayo_quest.ayo_quest.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.patterns.TypePatternQuestions;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MultiplaEscolhaEntity extends QuestaoEntity {

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlternativaEntity> alternativas;
}