package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.dto.DadosProfileDTO;
import br.com.ayo_quest.ayo_quest.enuns.TipoUsuario;
import br.com.ayo_quest.ayo_quest.models.ProfileEntity;
import br.com.ayo_quest.ayo_quest.repository.ProfileRepository;
import br.com.ayo_quest.ayo_quest.repository.TurmaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository repository;

    public List<ProfileEntity> getTutors() {
        return repository.findByRole(TipoUsuario.TUTOR);
    }

    public ProfileEntity getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public DadosProfileDTO getDados(Jwt jwt) {

        UUID id = UUID.fromString(jwt.getSubject());

        ProfileEntity profile = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile não encontrado"));

        return new DadosProfileDTO(
                profile.getName(),
                profile.getTxNomeExibicao(),
                profile.getEmail(),
                profile.getLocalizacao(),
                profile.getBio(),
                profile.getLevel(),
                profile.getXp()
        );
    }

    public DadosProfileDTO alterarDados(Jwt jwt, DadosProfileDTO dto){
        return repository.findById(UUID.fromString(jwt.getSubject()))
                .map(profile -> {
                    profile.setName(dto.getName());
                    profile.setTxNomeExibicao(dto.getTxNomeExibicao());
                    profile.setEmail(dto.getEmail());
                    profile.setLocalizacao(dto.getLocalizacao());
                    profile.setBio(dto.getBio());
                    repository.save(profile);
                    return new DadosProfileDTO(
                            profile.getName(),
                            profile.getTxNomeExibicao(),
                            profile.getEmail(),
                            profile.getLocalizacao(),
                            profile.getBio(),
                            profile.getLevel(),
                            profile.getXp()
                    );
                })
                .orElseThrow(() -> new RuntimeException("Profile não encontrado"));
    }


}
