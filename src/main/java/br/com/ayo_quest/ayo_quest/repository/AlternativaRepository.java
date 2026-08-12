package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.models.AlternativaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlternativaRepository extends JpaRepository<AlternativaEntity, Long> {

    List<AlternativaEntity> findByQuestaoId(Long questaoId);

    void deleteByQuestaoId(Long questaoId);

    @Modifying(flushAutomatically = true)
    @Query("DELETE FROM AlternativaEntity a WHERE a.questao.id IN :questaoIds")
    void deleteByQuestaoIdIn(List<Long> questaoIds);
}
