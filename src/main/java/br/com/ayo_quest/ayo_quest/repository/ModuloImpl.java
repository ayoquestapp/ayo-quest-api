package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.dto.ConteudoDTO;
import br.com.ayo_quest.ayo_quest.dto.ModuloCompletoDTO;
import br.com.ayo_quest.ayo_quest.dto.ModuloDTO;
import br.com.ayo_quest.ayo_quest.dto.resolver.AlternativaResolverDTO;
import br.com.ayo_quest.ayo_quest.dto.resolver.QuestaoResolverDTO;
import br.com.ayo_quest.ayo_quest.enuns.TipoConteudo;
import br.com.ayo_quest.ayo_quest.enuns.TipoQuestao;
import br.com.ayo_quest.ayo_quest.models.ModuloEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.engine.spi.ManagedEntity;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ModuloImpl {


    @PersistenceContext
    private EntityManager entityManager;


    public List<ModuloDTO> getAllModulos() {

        String sql = """
                SELECT 
                   m.id,
                   m.nome,
                   m.descricao,
                   m.carga_horaria,
                   m.xp_ao_concluir,
                   m.tempo_maximo,
                   m.trilha_id,
                   t.nome AS trilha_nome
                   FROM tbl_modulos m
                   LEFT JOIN tbl_trilhas t\s
                   ON t.id = m.trilha_id
                """;


        Query query = entityManager.createNativeQuery(sql, ModuloEntity.class);


        return query.getResultList();
    }

    public List<Long> buscarConteudosIdsPorModulo(Long moduloId) {

        String sql = """
                    SELECT c.id
                    FROM tbl_conteudo c
                    WHERE c.modulo_id = :moduloId
                """;


        return entityManager.createNativeQuery(sql).setParameter("moduloId", moduloId).getResultList();
    }


    public List<Long> buscarQuestoesIdsPorModulo(Long moduloId) {

        String sql = """
                    SELECT q.id
                    FROM tbl_questao q
                    WHERE q.modulo_id = :moduloId
                """;


        return entityManager.createNativeQuery(sql).setParameter("moduloId", moduloId).getResultList();
    }

    public List<QuestaoResolverDTO> buscarQuestoesParaValidacao(Long moduloId) {

        String sql = """
                SELECT
                    q.id,
                    q.enunciado,
                    q.tipo,
                    q.xp,
                    a.id,
                    a.texto,
                    a.correta
                
                FROM tbl_questao q
                
                INNER JOIN tbl_alternativa a
                    ON a.questao_id = q.id
                
                WHERE q.modulo_id = :moduloId
                
                ORDER BY q.id
                """;


        List<Object[]> resultado = entityManager.createNativeQuery(sql).setParameter("moduloId", moduloId).getResultList();


        Map<Long, QuestaoResolverDTO> questoes = new LinkedHashMap<>();


        for (Object[] row : resultado) {


            Long questaoId = ((Number) row[0]).longValue();


            QuestaoResolverDTO questao = questoes.computeIfAbsent(questaoId, id -> new QuestaoResolverDTO(id, (String) row[1], TipoQuestao.valueOf(row[2].toString()), ((Number) row[3]).intValue(), new ArrayList<>()));


            AlternativaResolverDTO alternativa = new AlternativaResolverDTO(((Number) row[4]).longValue(), (String) row[5], (Boolean) row[6]);


            questao.getAlternativas().add(alternativa);

        }


        return new ArrayList<>(questoes.values());
    }

    public ModuloCompletoDTO buscarModuloCompleto(Long id) {

        String sql = """
        SELECT
            m.id,
            m.nome,
            m.descricao,
            m.carga_horaria,
            m.xp_ao_concluir,
            m.tempo_maximo,
            t.id,
            t.nome
        FROM tbl_modulos m
        LEFT JOIN tbl_trilhas t
            ON t.id = m.trilha_id
        WHERE m.id = :id
    """;


        Object[] row = (Object[]) entityManager
                .createNativeQuery(sql)
                .setParameter("id", id)
                .getSingleResult();



        ModuloCompletoDTO modulo = ModuloCompletoDTO.builder()
                .id(((Number) row[0]).longValue())
                .nome((String) row[1])
                .descricao((String) row[2])
                .cargaHoraria(((Number) row[3]).longValue())
                .xpAoConcluir(((Number) row[4]).longValue())
                .tempoMaximo(
                        row[5] != null ? ((Number) row[5]).longValue() : null
                )
                .trilhaId(
                        row[6] != null ? ((Number) row[6]).longValue() : null
                )
                .nomeTrilha((String) row[7])
                .build();



        modulo.setConteudos(
                buscarConteudosPorModulo(id)
        );


        modulo.setQuestoes(
                buscarQuestoesPorModulo(id)
        );


        return modulo;
    }

    public List<ConteudoDTO> buscarConteudosPorModulo(Long moduloId){

        String sql = """
        SELECT
            c.id,
            c.titulo,
            c.valor,
            c.tipo
        FROM tbl_conteudo c
        WHERE c.modulo_id = :moduloId
    """;


        List<Object[]> rows = entityManager
                .createNativeQuery(sql)
                .setParameter("moduloId", moduloId)
                .getResultList();


        return rows.stream()
                .map(row -> ConteudoDTO.builder()
                        .id(((Number) row[0]).longValue())
                        .titulo((String) row[1])
                        .valor((String) row[2])
                        .tipo(TipoConteudo.valueOf(row[3].toString()))
                        .build())
                .toList();

    }

    public List<QuestaoResolverDTO> buscarQuestoesPorModulo(Long moduloId){

        String sql = """
        SELECT
            q.id,
            q.enunciado,
            q.tipo,
            q.xp,
            a.id,
            a.texto,
            a.correta

        FROM tbl_questao q

        LEFT JOIN tbl_alternativa a
            ON a.questao_id = q.id

        WHERE q.modulo_id = :moduloId

        ORDER BY q.id
    """;


        List<Object[]> rows = entityManager
                .createNativeQuery(sql)
                .setParameter("moduloId", moduloId)
                .getResultList();


        Map<Long, QuestaoResolverDTO> mapa = new LinkedHashMap<>();


        for(Object[] row : rows){

            Long idQuestao = ((Number)row[0]).longValue();


            QuestaoResolverDTO questao =
                    mapa.computeIfAbsent(
                            idQuestao,
                            key -> new QuestaoResolverDTO(
                                    idQuestao,
                                    (String) row[1],
                                    TipoQuestao.valueOf(row[2].toString()),
                                    ((Number)row[3]).intValue(),
                                    new ArrayList<>()
                            )
                    );


            if(row[4] != null){

                questao.getAlternativas()
                        .add(
                                new AlternativaResolverDTO(
                                        ((Number)row[4]).longValue(),
                                        (String) row[5],
                                        (Boolean) row[6]
                                )
                        );
            }

        }

        return new ArrayList<>(mapa.values());

    }
}