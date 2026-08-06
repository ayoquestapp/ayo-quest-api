package br.com.ayo_quest.ayo_quest.models;

import br.com.ayo_quest.ayo_quest.enuns.TipoUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
@Entity
@Table(name = "profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileEntity {


    @Id
    private UUID id;

    private String name;

    private String txNomeExibicao;

    private String avatar_url;

    private String bio;

    private String localizacao;


    @Enumerated(EnumType.STRING)
    private TipoUsuario role;


    private String email;


    private Long xp = 0L;


    private Integer level = 1;


    private Instant created_at;


    private Instant updated_at;


    @ManyToOne
    @JoinColumn(name="nivel_atual_id")
    private NivelEntity nivelAtual;



    @OneToOne
    @JoinColumn(name="usuario_id")
    private UsuarioEntity usuario;

}