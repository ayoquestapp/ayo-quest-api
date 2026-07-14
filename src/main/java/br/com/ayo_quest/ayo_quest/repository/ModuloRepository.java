package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.models.ModuloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModuloRepository extends JpaRepository<ModuloEntity, Long> {

    @Query("""
            SELECT m FROM ModuloEntity m
            JOIN FETCH m.trilha
            """)
    List<ModuloEntity> listarComTrilha();

    @Query
    List<ModuloEntity> findByTrilhaId(Long trilhaId);

    @Query("""
                SELECT COALESCE(SUM(m.cargaHoraria), 0)
                FROM ModuloEntity m
                WHERE m.trilha.id = :trilhaId
            """)
    Long calcularCargaHorariaPorTrilha(
            @Param("trilhaId") Long trilhaId
    );

    @Query("""
                SELECT COALESCE(SUM(m.xpAoConcluir), 0)
                FROM ModuloEntity m
                WHERE m.trilha.id = :trilhaId
            """)
    Long calcularXpTotalPorTrilha(
            @Param("trilhaId") Long trilhaId
    );

}
