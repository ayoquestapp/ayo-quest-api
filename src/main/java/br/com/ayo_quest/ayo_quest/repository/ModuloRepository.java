package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.dto.ModuloDTO;
import br.com.ayo_quest.ayo_quest.models.ModuloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModuloRepository extends JpaRepository<ModuloEntity, Long> {


    @Query("""
                SELECT new br.com.ayo_quest.ayo_quest.dto.ModuloDTO(
                    m.id,
                    m.nome,
                    m.descricao,
                    m.cargaHoraria,
                    m.xpAoConcluir,
                    m.trilhaId,
                    null,
                    null,
                    null,
                    m.tempoMaximo
                )
                FROM ModuloEntity m
                WHERE m.trilhaId = :trilhaId
            """)
    List<ModuloDTO> buscarPorTrilha(
            @Param("trilhaId") Long trilhaId
    );


    @Query("""
                SELECT COUNT(m)
                FROM ModuloEntity m
                WHERE m.trilhaId = :id
            """)
    Long contarModulosPorTrilha(
            @Param("id") Long id
    );


    @Query("""
                SELECT COALESCE(SUM(m.cargaHoraria), 0)
                FROM ModuloEntity m
                WHERE m.trilhaId = :trilhaId
            """)
    Long calcularCargaHorariaPorTrilha(
            @Param("trilhaId") Long trilhaId
    );


    @Query("""
                SELECT COALESCE(SUM(m.xpAoConcluir), 0)
                FROM ModuloEntity m
                WHERE m.trilhaId = :trilhaId
            """)
    Long calcularXpTotalPorTrilha(
            @Param("trilhaId") Long trilhaId
    );

}