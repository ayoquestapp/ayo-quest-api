package br.com.ayo_quest.ayo_quest.dto;

import br.com.ayo_quest.ayo_quest.enuns.TipoUsuario;
import br.com.ayo_quest.ayo_quest.models.ProfileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DadosProfileDTO {


    private String name;

    private String txNomeExibicao;

    private String email;

    private String localizacao;

    private TipoUsuario role;

    private String bio;

    private Integer level;

    private Long xp;



    public DadosProfileDTO(ProfileEntity profile) {


        this.name =
                profile.getName();


        this.txNomeExibicao =
                profile.getTxNomeExibicao();


        this.email =
                profile.getEmail();


        this.localizacao =
                profile.getLocalizacao();


        this.role =
                profile.getRole();


        this.bio =
                profile.getBio();


        this.level =
                profile.getLevel();


        this.xp =
                profile.getXp();

    }

}