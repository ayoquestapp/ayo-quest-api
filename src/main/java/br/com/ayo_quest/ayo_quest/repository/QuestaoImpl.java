package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.dto.AlternativaDTO;
import br.com.ayo_quest.ayo_quest.dto.QuestaoDTO;
import br.com.ayo_quest.ayo_quest.dto.resolver.AlternativaResolverDTO;
import br.com.ayo_quest.ayo_quest.dto.resolver.QuestaoResolverDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                ORDER BY q.id
                """;

        List<QuestaoDTO> questoes =
                entityManager
                        .createNativeQuery(
                                sql,
                                "QuestaoDTOMapping"
                        )
                        .setParameter(
                                "moduloId",
                                moduloId
                        )
                        .getResultList();

        if (questoes == null || questoes.isEmpty()) {

            return Collections.emptyList();
        }

        List<Long> questaoIds =
                questoes.stream()
                        .map(QuestaoDTO::getId)
                        .toList();


        System.out.println(
                "QUESTÕES ENCONTRADAS: "
                        + questaoIds
        );

        List<AlternativaResolverDTO> alternativas =
                alternativaImpl.buscarAlternativasPorQuestao(
                        questaoIds
                );


        if (alternativas == null) {

            alternativas = Collections.emptyList();
        }


        System.out.println(
                "ALTERNATIVAS ENCONTRADAS: "
                        + alternativas.size()
        );

        Map<Long, List<AlternativaResolverDTO>>
                alternativasPorQuestao =

                alternativas.stream()
                        .collect(
                                Collectors.groupingBy(
                                        AlternativaResolverDTO::getQuestaoId
                                )
                        );

        for (QuestaoDTO questao : questoes) {

            List<AlternativaResolverDTO>
                    alternativasDaQuestao =

                    alternativasPorQuestao.getOrDefault(
                            questao.getId(),
                            Collections.emptyList()
                    );


            List<AlternativaDTO>
                    alternativasDTO =

                    alternativasDaQuestao.stream()
                            .map(alternativa ->
                                    AlternativaDTO.builder()
                                            .id(alternativa.getId())
                                            .texto(alternativa.getTexto())
                                            .correta(alternativa.isCorreta())
                                            .build()
                            )
                            .toList();


            questao.setAlternativas(
                    alternativasDTO
            );

            System.out.println(
                    "QUESTÃO "
                            + questao.getId()
                            + " → ALTERNATIVAS: "
                            + alternativasDTO
            );
        }


        return questoes;
    }

    public List<QuestaoResolverDTO> buscarQuestoesParaValidacao(
            Long moduloId
    ) {

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

                entityManager
                        .createNativeQuery(
                                sql,
                                "QuestaoResolverMapping"
                        )
                        .setParameter(
                                "moduloId",
                                moduloId
                        )
                        .getResultList();


        if (questoes == null || questoes.isEmpty()) {

            return Collections.emptyList();
        }

        List<Long> ids =

                questoes.stream()
                        .map(
                                QuestaoResolverDTO::getId
                        )
                        .toList();

        List<AlternativaResolverDTO> alternativas =

                alternativaImpl.buscarAlternativasPorQuestao(
                        ids
                );


        if (alternativas == null) {

            alternativas = Collections.emptyList();
        }

        Map<Long, List<AlternativaResolverDTO>>
                alternativasPorQuestao =

                alternativas.stream()
                        .collect(
                                Collectors.groupingBy(
                                        AlternativaResolverDTO::getQuestaoId
                                )
                        );

        for (QuestaoResolverDTO questao : questoes) {

            List<AlternativaResolverDTO>
                    alternativasDaQuestao =

                    alternativasPorQuestao.getOrDefault(
                            questao.getId(),
                            Collections.emptyList()
                    );


            questao.setAlternativas(
                    alternativasDaQuestao
            );
        }

        return questoes;
    }
}