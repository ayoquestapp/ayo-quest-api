package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.dto.tentativaModulo.IniciarTentativaDTO;
import br.com.ayo_quest.ayo_quest.dto.tentativaModulo.TentativaDTO;
import br.com.ayo_quest.ayo_quest.enuns.StatusTentativa;
import br.com.ayo_quest.ayo_quest.models.ModuloEntity;
import br.com.ayo_quest.ayo_quest.models.ProfileEntity;
import br.com.ayo_quest.ayo_quest.models.TentativaModuloEntity;
import br.com.ayo_quest.ayo_quest.repository.ModuloRepository;
import br.com.ayo_quest.ayo_quest.repository.ProfileRepository;
import br.com.ayo_quest.ayo_quest.repository.TentativaModuloRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TentativaModuloService {


    private final TentativaModuloRepository tentativaRepository;

    private final ModuloRepository moduloRepository;

    private final ProfileRepository profileRepository;


    @Transactional
    public TentativaDTO iniciar(
            IniciarTentativaDTO dto,
            UUID profileId
    ) {

        ModuloEntity modulo =
                moduloRepository.findById(dto.getModuloId())
                        .orElseThrow(() ->
                                new RuntimeException("Módulo não encontrado")
                        );


        ProfileEntity profile =
                profileRepository.findById(profileId)
                        .orElseThrow(() ->
                                new RuntimeException("Perfil não encontrado")
                        );


        TentativaModuloEntity tentativa =
                new TentativaModuloEntity();


        tentativa.setModulo(modulo);

        tentativa.setProfile(profile);

        tentativa.setInicio(LocalDateTime.from(Instant.now()));

        tentativa.setStatus(StatusTentativa.EM_ANDAMENTO);


        TentativaModuloEntity salva =
                tentativaRepository.save(tentativa);


        return new TentativaDTO(
                salva.getId(),
                salva.getStatus()
        );
    }

}
