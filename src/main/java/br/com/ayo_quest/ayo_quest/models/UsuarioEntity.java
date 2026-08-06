package br.com.ayo_quest.ayo_quest.models;

import br.com.ayo_quest.ayo_quest.enuns.TipoUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Entity
@Table(name = "tbl_usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEntity {

    @Id
    private UUID id;


    private String nome;


    @Column(unique = true, nullable = false)
    private String email;


    @Column(nullable = false)
    private String senha;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario role;


    @Builder.Default
    private boolean ativo = false;


    private String tokenConfirmacao;



    @OneToOne(
            mappedBy = "usuario",
            cascade = CascadeType.ALL
    )
    private ProfileEntity profile;

}