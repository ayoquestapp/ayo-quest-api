package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.models.TentativaModuloEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TentativaModuloRepository
        extends JpaRepository<TentativaModuloEntity, Long> {

    boolean existsByProfileIdAndModuloId(
            UUID profileId,
            Long moduloId
    );

}
