package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.models.TrilhaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrilhaRepository extends JpaRepository<TrilhaEntity , Long> {

    @Query("""
SELECT DISTINCT t FROM TrilhaEntity t
LEFT JOIN FETCH t.modulos
""")
    List<TrilhaEntity> listarComModulos();

}
