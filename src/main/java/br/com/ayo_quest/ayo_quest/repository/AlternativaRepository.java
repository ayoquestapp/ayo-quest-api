package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.models.AlternativaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlternativaRepository extends JpaRepository<AlternativaEntity,Long> {
    List<AlternativaEntity> findByQuestaoId(Long questaoId);

    @Modifying
    @Query("DELETE FROM AlternativaEntity a WHERE a.questao.id = :questaoId")
    void deleteByQuestaoId(Long questaoId);
}
