package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.dto.DadosProfileDTO;
import br.com.ayo_quest.ayo_quest.enuns.TipoUsuario;
import br.com.ayo_quest.ayo_quest.models.ProfileEntity;
import br.com.ayo_quest.ayo_quest.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {


    private final ProfileRepository repository;



    public List<ProfileEntity> getTutors() {

        return repository.findByRole(
                TipoUsuario.TUTOR
        );

    }



    public ProfileEntity getById(java.util.UUID id) {

        return repository.findById(id)

                .orElseThrow(() ->
                        new RuntimeException(
                                "Profile não encontrado"
                        )
                );

    }



    public DadosProfileDTO getDados(
            Authentication authentication
    ){

        UUID usuarioId =
                UUID.fromString(
                        authentication.getName()
                );


        ProfileEntity profile =

                repository
                        .findByUsuarioId(usuarioId)

                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Profile não encontrado"
                                        )
                        );


        return new DadosProfileDTO(
                profile
        );

    }




    public DadosProfileDTO alterarDados(
            Authentication authentication,
            DadosProfileDTO dto
    ) {


        String email =
                authentication.getName();



        ProfileEntity profile =
                repository.findByEmail(email)

                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Profile não encontrado"
                                )
                        );



        profile.setName(dto.getName());

        profile.setTxNomeExibicao(
                dto.getTxNomeExibicao()
        );

        profile.setEmail(
                dto.getEmail()
        );

        profile.setLocalizacao(
                dto.getLocalizacao()
        );



        profile.setBio(
                dto.getBio()
        );



        repository.save(profile);



        return converter(profile);

    }




    private DadosProfileDTO converter(
            ProfileEntity profile
    ){

        return new DadosProfileDTO(

                profile.getName(),

                profile.getTxNomeExibicao(),

                profile.getEmail(),

                profile.getLocalizacao(),
                profile.getRole(),

                profile.getBio(),

                profile.getLevel(),

                profile.getXp()

        );

    }

}