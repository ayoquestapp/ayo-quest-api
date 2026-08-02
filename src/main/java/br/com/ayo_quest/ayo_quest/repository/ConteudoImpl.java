package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.dto.ConteudoDTO;
import br.com.ayo_quest.ayo_quest.dto.resolver.ConteudoResolverDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConteudoImpl {
    @PersistenceContext
    private EntityManager entityManager;

    public List<ConteudoDTO> buscarConteudosPorModulo(Long moduloId){

        String sql = """
        SELECT
            c.id,
            c.tipo,
            c.valor,
            c.titulo
        FROM tbl_conteudo c

        WHERE c.modulo_id = :moduloId
    """;


        return entityManager
                .createNativeQuery(sql, "ConteudoResolverMapping")
                .setParameter("moduloId", moduloId)
                .getResultList();
    }
}
