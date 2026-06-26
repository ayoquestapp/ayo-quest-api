package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.enuns.TipoUsuario;
import br.com.ayo_quest.ayo_quest.models.ProfileEntity;
import br.com.ayo_quest.ayo_quest.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
