package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.models.LevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LevelRepository extends JpaRepository<LevelEntity, Long> {


    @Query("""
        SELECT l
        FROM LevelEntity l
        WHERE l.xpNecessario <= :xp
        ORDER BY l.xpNecessario DESC
    """)
    Optional<LevelEntity> buscarLevelPorXp(
            @Param("xp") Integer xp
    );

}
