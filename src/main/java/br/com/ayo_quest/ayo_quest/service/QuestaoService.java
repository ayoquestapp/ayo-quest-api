package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.dto.QuestaoDTO;
import br.com.ayo_quest.ayo_quest.models.ModuloEntity;
import br.com.ayo_quest.ayo_quest.models.QuestaoEntity;
import br.com.ayo_quest.ayo_quest.repository.QuestaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestaoService {

    private final QuestaoRepository questaoRepository;
    private final AlternativaService alternativaService;


    public List<QuestaoDTO> buscarPorModulo(Long moduloId) {

        return questaoRepository.findByModuloId(moduloId)
                .stream()
                .map(this::converter)
                .toList();
    }


    @Transactional
    public void salvarLista(
            ModuloEntity modulo,
            List<QuestaoDTO> questoes
    ) {

        if (questoes == null || questoes.isEmpty()) {
            return;
        }

        List<QuestaoEntity> entidades = questoes.stream()
                .map(dto -> {

                    QuestaoEntity questao = new QuestaoEntity();

                    questao.setEnunciado(dto.getEnunciado());
                    questao.setTipo(dto.getTipo());
                    questao.setXp(dto.getXp());
                    questao.setTempoPorQuestao(dto.getTempoPorQuestao());
                    questao.setModulo(modulo);

                    return questao;

                })
                .toList();
        List<QuestaoEntity> salvas =
                questaoRepository.saveAll(entidades);

        for (int i = 0; i < salvas.size(); i++) {

            QuestaoEntity questao = salvas.get(i);

            QuestaoDTO dto = questoes.get(i);

            alternativaService.salvarLista(
                    questao,
                    dto.getAlternativas()
            );
        }
    }


    @Transactional
    public void atualizarLista(
            ModuloEntity modulo,
            List<QuestaoDTO> questoesDTO
    ) {

        if (questoesDTO == null) {
            questoesDTO = List.of();
        }

        List<QuestaoEntity> existentes =
                questaoRepository.findByModuloId(modulo.getId());

        Map<Long, QuestaoEntity> existentesMap =
                existentes.stream()
                        .filter(q -> q.getId() != null)
                        .collect(Collectors.toMap(
                                QuestaoEntity::getId,
                                Function.identity()
                        ));

        Set<Long> idsRecebidos =
                questoesDTO.stream()
                        .map(QuestaoDTO::getId)
                        .filter(id -> id != null)
                        .collect(Collectors.toSet());


        List<Long> idsRemover =
                existentes.stream()
                        .map(QuestaoEntity::getId)
                        .filter(id -> !idsRecebidos.contains(id))
                        .toList();

        if (!idsRemover.isEmpty()) {

            alternativaService.deletarPorQuestoes(idsRemover);

            questaoRepository.deleteByIdIn(idsRemover);
        }

        questoesDTO.forEach(dto -> {

            QuestaoEntity questao;

            if (dto.getId() != null && existentesMap.containsKey(dto.getId())) {
                /*
                 * EXISTENTE:
                 * reaproveita a entidade carregada.
                 */
                questao = existentesMap.get(dto.getId());

            } else {
                /*
                 * NOVA:
                 * cria somente se realmente for nova.
                 */
                questao = new QuestaoEntity();
                questao.setModulo(modulo);
            }

            questao.setEnunciado(dto.getEnunciado());
            questao.setTipo(dto.getTipo());
            questao.setXp(dto.getXp());
            questao.setTempoPorQuestao(dto.getTempoPorQuestao());

            /*
             * Salva a questão (INSERT ou UPDATE) para garantir que temos um ID
             * para passar para o serviço de alternativas.
             */
            QuestaoEntity questaoSalva = questaoRepository.save(questao);

            /*
             * Agora, com a questão salva e seu ID garantido, atualizamos a lista
             * de alternativas correspondente a ela.
             */
            if (dto.getAlternativas() != null) {
                alternativaService.atualizarLista(
                        questaoSalva,
                        dto.getAlternativas()
                );
            }
        });
    }


    @Transactional
    public void deletarPorModulo(Long moduloId) {

        List<QuestaoEntity> questoes =
                questaoRepository.findByModuloId(moduloId);

        if (questoes.isEmpty()) {
            return;
        }

        List<Long> ids =
                questoes.stream()
                        .map(QuestaoEntity::getId)
                        .toList();

        alternativaService.deletarPorQuestoes(ids);

        questaoRepository.deleteAllInBatch(questoes);
    }


    private QuestaoDTO converter(QuestaoEntity entity) {

        QuestaoDTO dto = new QuestaoDTO();

        dto.setId(entity.getId());
        dto.setEnunciado(entity.getEnunciado());
        dto.setTipo(entity.getTipo());
        dto.setXp(entity.getXp());
        dto.setTempoPorQuestao(
                entity.getTempoPorQuestao()
        );

        dto.setAlternativas(
                alternativaService.buscarPorQuestao(
                        entity.getId()
                )
        );

        return dto;
    }
}
