package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.dto.PeriodoDTO;
import br.com.ayo_quest.ayo_quest.enuns.TiposPeriodos;
import br.com.ayo_quest.ayo_quest.models.TurmaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TurmaRepository extends JpaRepository<TurmaEntity,Long> {


}
