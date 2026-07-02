package br.com.ayo_quest.ayo_quest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TBL_TRILHAS")
public class TrilhaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String code;

    private String descricao;

    private String  imagem;


    @OneToMany(mappedBy = "trilha")
    @JsonIgnore
    private List<ModuloEntity> modulos;

    @ManyToMany(mappedBy = "trilhas")
    private List<TurmaEntity> turmas = new ArrayList<>();
}
