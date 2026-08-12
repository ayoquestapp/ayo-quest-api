package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.dto.AlternativaDTO;
import br.com.ayo_quest.ayo_quest.models.AlternativaEntity;
import br.com.ayo_quest.ayo_quest.models.QuestaoEntity;
import br.com.ayo_quest.ayo_quest.repository.AlternativaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlternativaService {

    private final AlternativaRepository alternativaRepository;


    public List<AlternativaDTO> buscarPorQuestao(Long questaoId) {

        return alternativaRepository.findByQuestaoId(questaoId)
                .stream()
                .map(this::converter)
                .toList();
    }


    public void salvarLista(
            QuestaoEntity questao,
            List<AlternativaDTO> alternativas
    ) {

        if (alternativas == null || alternativas.isEmpty()) {
            return;
        }

        List<AlternativaEntity> entidades = alternativas.stream()
                .map(dto -> {

                    AlternativaEntity alternativa = new AlternativaEntity();

                    alternativa.setTexto(dto.getTexto());
                    alternativa.setCorreta(dto.isCorreta());
                    alternativa.setQuestao(questao);

                    return alternativa;
                })
                .toList();

        alternativaRepository.saveAll(entidades);
    }


    @Transactional
    public void atualizarLista(
            QuestaoEntity questao,
            List<AlternativaDTO> alternativasDTO
    ) {

        if (alternativasDTO == null) {
            alternativasDTO = List.of();
        }

        List<AlternativaEntity> existentes =
                alternativaRepository.findByQuestaoId(questao.getId());

        if (alternativasDTO.isEmpty()) {

            if (!existentes.isEmpty()) {
                alternativaRepository.deleteAllInBatch(existentes);
            }

            return;
        }

        var idsRecebidos = alternativasDTO.stream()
                .map(AlternativaDTO::getId)
                .filter(id -> id != null)
                .collect(java.util.stream.Collectors.toSet());


        List<AlternativaEntity> remover =
                existentes.stream()
                        .filter(alternativa ->
                                !idsRecebidos.contains(alternativa.getId()))
                        .toList();


        if (!remover.isEmpty()) {
            alternativaRepository.deleteAllInBatch(remover);
        }

        var existentesMap = existentes.stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                AlternativaEntity::getId,
                                alternativa -> alternativa
                        )
                );

        List<AlternativaEntity> salvar = alternativasDTO.stream()
                .map(dto -> {

                    AlternativaEntity alternativa;

                    if (dto.getId() != null &&
                            existentesMap.containsKey(dto.getId())) {

                        alternativa = existentesMap.get(dto.getId());

                    } else {

                        alternativa = new AlternativaEntity();
                        alternativa.setQuestao(questao);
                    }

                    alternativa.setTexto(dto.getTexto());
                    alternativa.setCorreta(dto.isCorreta());

                    return alternativa;

                })
                .toList();


        /*
         * UMA operação para salvar tudo.
         */
        alternativaRepository.saveAll(salvar);
    }


    @Transactional
    public void deletarPorQuestao(Long questaoId) {

        alternativaRepository.deleteByQuestaoId(questaoId);
    }


    private AlternativaDTO converter(AlternativaEntity entity) {

        AlternativaDTO dto = new AlternativaDTO();

        dto.setId(entity.getId());
        dto.setTexto(entity.getTexto());
        dto.setCorreta(entity.isCorreta());

        return dto;
    }

    @Transactional
    public void deletarPorQuestoes(List<Long> questaoIds) {

        if (questaoIds == null || questaoIds.isEmpty()) {
            return;
        }

        alternativaRepository.deleteByQuestaoIdIn(questaoIds);
    }
}
