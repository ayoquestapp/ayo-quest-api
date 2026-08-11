package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.models.ProgressoUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProgressoUsuarioRepository
        extends JpaRepository<ProgressoUsuarioEntity, Long> {


    Optional<ProgressoUsuarioEntity> findByProfileId(UUID profileId);

}
