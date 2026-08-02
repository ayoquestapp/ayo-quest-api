package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.dto.QuestaoDTO;
import br.com.ayo_quest.ayo_quest.models.ModuloEntity;
import br.com.ayo_quest.ayo_quest.models.QuestaoEntity;
import br.com.ayo_quest.ayo_quest.repository.QuestaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestaoService {

    private final QuestaoRepository questaoRepository;
    private final AlternativaService alternativaService;


    public List<QuestaoDTO> buscarPorModulo(Long moduloId){

        return questaoRepository.findByModuloId(moduloId)
                .stream()
                .map(this::converter)
                .toList();
    }

    public void salvarLista(
            ModuloEntity modulo,
            List<QuestaoDTO> questoes
    ){


        questoes.forEach(dto -> {


            QuestaoEntity questao = new QuestaoEntity();


            questao.setEnunciado(dto.getEnunciado());
            questao.setTipo(dto.getTipo());
            questao.setXp(dto.getXp());

            questao.setModulo(modulo);



            QuestaoEntity salva =
                    questaoRepository.save(questao);



            if(dto.getAlternativas() != null){

                alternativaService.salvarLista(
                        salva,
                        dto.getAlternativas()
                );

            }

        });

    }
    @Transactional
    public void atualizarLista(
            ModuloEntity modulo,
            List<QuestaoDTO> questoesDTO
    ) {

        List<QuestaoEntity> existentes =
                questaoRepository.findByModuloId(modulo.getId());

        existentes.forEach(questao -> {

            boolean existe = questoesDTO.stream()
                    .anyMatch(dto ->
                            dto.getId() != null &&
                                    dto.getId().equals(questao.getId()));

            if (!existe) {

                alternativaService.deletarPorQuestao(questao.getId());

                questaoRepository.delete(questao);

            }

        });

        questoesDTO.forEach(dto -> {

            QuestaoEntity questao =
                    dto.getId() != null
                            ? questaoRepository.findById(dto.getId()).orElse(new QuestaoEntity())
                            : new QuestaoEntity();

            questao.setEnunciado(dto.getEnunciado());
            questao.setTipo(dto.getTipo());
            questao.setXp(dto.getXp());
            questao.setModulo(modulo);

            QuestaoEntity salva =
                    questaoRepository.save(questao);

            alternativaService.atualizarLista(
                    salva,
                    dto.getAlternativas()
            );

        });

    }

    @Transactional
    public void deletarPorModulo(Long moduloId) {

        List<QuestaoEntity> questoes =
                questaoRepository.findByModuloId(moduloId);

        questoes.forEach(q -> {

            alternativaService.deletarPorQuestao(q.getId());

            questaoRepository.delete(q);

        });

    }


    private QuestaoDTO converter(QuestaoEntity entity){

        QuestaoDTO dto = new QuestaoDTO();

        dto.setId(entity.getId());
        dto.setEnunciado(entity.getEnunciado());
        dto.setTipo(entity.getTipo());
        dto.setXp(entity.getXp());


        dto.setAlternativas(
                alternativaService.buscarPorQuestao(entity.getId())
        );


        return dto;
    }
}
