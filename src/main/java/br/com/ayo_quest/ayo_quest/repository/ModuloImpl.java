package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.dto.ConteudoDTO;
import br.com.ayo_quest.ayo_quest.dto.ModuloCompletoDTO;
import br.com.ayo_quest.ayo_quest.dto.ModuloDTO;
import br.com.ayo_quest.ayo_quest.dto.ResultadoModuloDTO;
import br.com.ayo_quest.ayo_quest.dto.resolver.AlternativaResolverDTO;
import br.com.ayo_quest.ayo_quest.dto.resolver.ModuloResolverDTO;
import br.com.ayo_quest.ayo_quest.dto.resolver.QuestaoResolverDTO;
import br.com.ayo_quest.ayo_quest.enuns.TipoConteudo;
import br.com.ayo_quest.ayo_quest.enuns.TipoQuestao;
import br.com.ayo_quest.ayo_quest.models.ModuloEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.engine.spi.ManagedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ModuloImpl {


    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ConteudoImpl conteudoImpl;

    @Autowired
    private QuestaoImpl questaoImpl;


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
            t.nome AS nome_trilha
        FROM tbl_modulos m
        LEFT JOIN tbl_trilhas t
            ON t.id = m.trilha_id
        """;

        List<ModuloDTO> modulos = entityManager
                .createNativeQuery(sql, "ModuloDTOMapping")
                .getResultList();

        for (ModuloDTO modulo : modulos) {

            modulo.setConteudos(
                    conteudoImpl.buscarConteudosPorModulo(modulo.getId())
            );

            modulo.setQuestoes(
                    questaoImpl.buscarQuestoesPorModulo(modulo.getId())
            );
        }

        return modulos;
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

                json_agg(
                    json_build_object(
                        'id', a.id,
                        'texto', a.texto,
                        'correta', a.correta
                    )
                ) AS alternativas

            FROM tbl_questao q

            LEFT JOIN tbl_alternativa a
                ON a.questao_id = q.id

            WHERE q.modulo_id = :moduloId

            GROUP BY
                q.id,
                q.enunciado,
                q.tipo,
                q.xp
            """;


        return entityManager
                .createNativeQuery(sql, "QuestaoResolverMapping")
                .setParameter("moduloId", moduloId)
                .getResultList();
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
                t.id as trilhaId,
                t.nome as nomeTrilha
            FROM tbl_modulos m
            LEFT JOIN tbl_trilhas t
                ON t.id = m.trilha_id
            WHERE m.id = :id
        """;

        ModuloCompletoDTO modulo = (ModuloCompletoDTO) entityManager
                .createNativeQuery(sql, "ModuloCompletoDTOMapping")
                .setParameter("id", id)
                .getSingleResult();

        modulo.setConteudos(buscarConteudosPorModulo(id));
        modulo.setQuestoes(buscarQuestoesPorModulo(id));

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

        return entityManager
                .createNativeQuery(sql, "ConteudoDTOMapping")
                .setParameter("moduloId", moduloId)
                .getResultList();
    }

    public List<QuestaoResolverDTO> buscarQuestoesPorModulo(Long moduloId){

        String sql = """
        SELECT
            q.id AS id,
            q.enunciado,
            q.tipo,
            q.xp

        FROM tbl_questao q

        WHERE q.modulo_id = :moduloId

        ORDER BY q.id
    """;


        return entityManager
                .createNativeQuery(sql, "QuestaoResolverMapping")
                .setParameter("moduloId", moduloId)
                .getResultList();

    }

    public ModuloResolverDTO buscarModuloResolver(Long id) {

        String sql = """
        SELECT
            m.id,
            m.nome,
            m.descricao,
            m.carga_horaria,
            m.xp_ao_concluir

        FROM tbl_modulos m

        WHERE m.id = :id
    """;


        ModuloResolverDTO modulo = (ModuloResolverDTO)
                entityManager
                        .createNativeQuery(sql, "ModuloResolverDTOMapping")
                        .setParameter("id", id)
                        .getSingleResult();


        modulo.setQuestoes(
                buscarQuestoesParaResolver(id)
        );


        return modulo;
    }

    public List<QuestaoResolverDTO> buscarQuestoesParaResolver(Long moduloId){

        String sql = """
        SELECT
            q.id,
            q.enunciado,
            q.tipo,
            q.xp,

            json_agg(
                json_build_object(
                    'id', a.id,
                    'texto', a.texto
                )
            ) AS alternativas

        FROM tbl_questao q

        LEFT JOIN tbl_alternativa a
            ON a.questao_id = q.id

        WHERE q.modulo_id = :moduloId

        GROUP BY
            q.id,
            q.enunciado,
            q.tipo,
            q.xp

        ORDER BY q.id
    """;


        return entityManager
                .createNativeQuery(sql, "QuestaoResolverMapping")
                .setParameter("moduloId", moduloId)
                .getResultList();
    }

    public ResultadoModuloDTO conferirRespostas(
            Long moduloId,
            Map<String,Object> respostas
    ){

        List<QuestaoResolverDTO> questoes =
                buscarQuestoesParaValidacao(moduloId);


        int acertos = 0;
        int xp = 0;


        for(QuestaoResolverDTO questao : questoes){

            Object resposta =
                    respostas.get(
                            String.valueOf(questao.getId())
                    );


            boolean acertou =
                    questao.getAlternativas() != null &&
                    questao.getAlternativas()
                            .stream()
                            .anyMatch(a ->
                                    a.isCorreta()
                                            &&
                                            a.getId()
                                                    .toString()
                                                    .equals(resposta.toString())
                            );


            if(acertou){
                acertos++;
                xp += questao.getXp();
            }
        }


        return ResultadoModuloDTO.builder()
                .totalQuestoes((long) questoes.size())
                .acertos(acertos)
                .erros(questoes.size()-acertos)
                .xpGanho(xp)
                .aprovado(acertos >= questoes.size()*0.7)
                .build();
    }
}