package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.models.ModuloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ModuloRepository extends JpaRepository<ModuloEntity, Long> {

    @Query("""
SELECT m FROM ModuloEntity m
JOIN FETCH m.trilha
""")
    List<ModuloEntity> listarComTrilha();

}
