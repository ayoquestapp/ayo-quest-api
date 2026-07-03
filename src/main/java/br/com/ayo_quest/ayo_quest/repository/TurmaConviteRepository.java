package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.enuns.StatusUsuarioTurmaEnum;
import br.com.ayo_quest.ayo_quest.models.TurmaConviteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TurmaConviteRepository extends JpaRepository<TurmaConviteEntity, Long> {

    boolean existsByTurmaIdAndEmail(Long turmaId, String email);

    Optional<TurmaConviteEntity> findByToken(String token);

    boolean existsByTurmaIdAndEmailAndStatus(
            Long turmaId,
            String email,
            StatusUsuarioTurmaEnum status
    );

    public Optional<TurmaConviteEntity> deleteByTurmaId(Long turmaId);
}