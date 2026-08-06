package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.dto.QuestaoDTO;
import br.com.ayo_quest.ayo_quest.dto.resolver.AlternativaResolverDTO;
import br.com.ayo_quest.ayo_quest.dto.resolver.QuestaoResolverDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestaoImpl {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AlternativaImpl alternativaImpl;

    public List<QuestaoDTO> buscarQuestoesPorModulo(Long moduloId) {

        String sql = """
                    SELECT
                        q.id,
                        q.enunciado,
                        q.tipo,
                        q.xp,
                        q.tempo_por_questao
                    FROM tbl_questao q
                
                    WHERE q.modulo_id = :moduloId
                """;

        return entityManager
                .createNativeQuery(sql, "QuestaoDTOMapping")
                .setParameter("moduloId", moduloId)
                .getResultList();
    }

    public List<QuestaoResolverDTO> buscarQuestoesParaValidacao(Long moduloId){

        String sql = """
        SELECT
            q.id,
            q.enunciado,
            q.tipo,
            q.xp,
            q.tempo_por_questao
        FROM tbl_questao q
        WHERE q.modulo_id = :moduloId
        ORDER BY q.id
        """;

        List<QuestaoResolverDTO> questoes =
                entityManager.createNativeQuery(sql, "QuestaoResolverMapping")
                        .setParameter("moduloId", moduloId)
                        .getResultList();

        List<Long> ids = questoes.stream()
                .map(QuestaoResolverDTO::getId)
                .toList();

        List<AlternativaResolverDTO> alternativas =
                alternativaImpl.buscarAlternativasPorQuestao(ids);

        return questoes;
    }
}
