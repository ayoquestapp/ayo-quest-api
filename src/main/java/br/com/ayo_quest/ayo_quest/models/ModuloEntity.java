package br.com.ayo_quest.ayo_quest.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

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
    private Long xpAoConcluir;

    @Column(name = "trilha_id")
    private Long trilhaId;

    private Long tempoMaximo;
}
