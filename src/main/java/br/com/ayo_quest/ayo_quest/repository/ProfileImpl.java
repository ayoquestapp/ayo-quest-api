package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.dto.ResponsavelDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.rmi.server.UID;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileImpl {
    @PersistenceContext
    public EntityManager entityManager;

    public ResponsavelDTO detalharResposanvel(UUID uuid) {
        String sql = getResponsavel();
        Query query = entityManager.createNativeQuery(sql, "ResponsavelDTOMapping");
        query.setParameter("id", uuid);

        return (ResponsavelDTO) query.getSingleResult();
    }

    private String getResponsavel() {
        return """
                SELECT
                   p.id AS id,
                   p.name AS nome,
                   p.email AS email
                FROM profiles p
                WHERE p.id = :id
                """;
    }

}
