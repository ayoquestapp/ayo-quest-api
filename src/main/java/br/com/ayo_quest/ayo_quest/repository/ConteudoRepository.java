package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.models.ConteudoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ConteudoRepository
        extends JpaRepository<ConteudoEntity, Long> {

    List<ConteudoEntity> findByModuloId(Long moduloId);

    @Modifying
    @Query("""
        DELETE FROM ConteudoEntity c
        WHERE c.id IN :ids
    """)
    void deleteByIdIn(
            @Param("ids") Collection<Long> ids
    );

    @Modifying
    @Query("""
        DELETE FROM ConteudoEntity c
        WHERE c.modulo.id = :moduloId
    """)
    void deleteByModuloId(
            @Param("moduloId") Long moduloId
    );
}
