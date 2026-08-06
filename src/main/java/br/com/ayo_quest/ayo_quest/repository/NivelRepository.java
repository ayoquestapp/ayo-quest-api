package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.models.NivelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NivelRepository extends JpaRepository<NivelEntity, Long> {
    List<NivelEntity> findByStatusTrueOrderByOrdemAsc();

}
