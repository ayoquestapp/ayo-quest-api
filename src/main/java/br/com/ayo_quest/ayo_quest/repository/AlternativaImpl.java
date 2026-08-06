package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.dto.resolver.AlternativaResolverDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AlternativaImpl {
    @PersistenceContext
    private EntityManager entityManager;

    public List<AlternativaResolverDTO> buscarAlternativasPorQuestao(List<Long> ids) {

        String sql = """
        SELECT
            a.id,
            a.texto,
            a.correta,
            a.questao_id
        FROM tbl_alternativa a
        WHERE a.questao_id IN (:ids)
        ORDER BY a.questao_id, a.id
    """;

        return entityManager
                .createNativeQuery(sql, "AlternativaResolverMapping")
                .setParameter("ids", ids)
                .getResultList();
    }
}
