package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.models.QuestaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Repository
public interface QuestaoRepository extends JpaRepository<QuestaoEntity, Long> {

    List<QuestaoEntity> findByModuloId(Long id);

    List<QuestaoEntity> findByProficienciaId(Long proficienciaId);

    @Modifying
    @Query("""
    DELETE FROM QuestaoEntity q
    WHERE q.id IN :ids
""")
    void deleteByIdIn(@Param("ids") Collection<Long> ids);
}
