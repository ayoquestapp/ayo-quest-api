package br.com.ayo_quest.ayo_quest.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tbl_nivel")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NivelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeNivel;

    private String descricao;

    private Integer ordem;

    private Boolean status;
}