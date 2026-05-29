package br.com.ayo_quest.ayo_quest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TBL_MODULOS")
public class ModuloEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    private Long cargaHoraria;

    @Column(name = "xp_ao_concluir")
    private Integer xpAoConcluir;

    @ManyToOne
    TrilhaEntity trilha;
    
    @JsonIgnore
    @OneToMany(mappedBy = "modulo", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<ConteudoEntity> conteudos;

    @JsonIgnore
    @OneToMany(mappedBy = "modulo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestaoEntity> questoes;
}