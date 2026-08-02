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


    public List<AlternativaDTO> buscarPorQuestao(Long questaoId){

        return alternativaRepository.findByQuestaoId(questaoId)
                .stream()
                .map(this::converter)
                .toList();

    }

    public void salvarLista(
            QuestaoEntity questao,
            List<AlternativaDTO> alternativas
    ){


        alternativas.forEach(dto -> {


            AlternativaEntity alternativa =
                    new AlternativaEntity();


            alternativa.setTexto(dto.getTexto());

            alternativa.setCorreta(dto.isCorreta());

            alternativa.setQuestao(questao);



            alternativaRepository.save(alternativa);

        });

    }

    @Transactional
    public void atualizarLista(
            QuestaoEntity questao,
            List<AlternativaDTO> alternativasDTO
    ) {

        List<AlternativaEntity> existentes =
                alternativaRepository.findByQuestaoId(questao.getId());

        existentes.forEach(alternativa -> {

            boolean existe = alternativasDTO.stream()
                    .anyMatch(dto ->
                            dto.getId() != null &&
                                    dto.getId().equals(alternativa.getId()));

            if (!existe) {
                alternativaRepository.delete(alternativa);
            }

        });

        alternativasDTO.forEach(dto -> {

            AlternativaEntity alternativa =
                    dto.getId() != null
                            ? alternativaRepository.findById(dto.getId()).orElse(new AlternativaEntity())
                            : new AlternativaEntity();

            alternativa.setTexto(dto.getTexto());
            alternativa.setCorreta(dto.isCorreta());
            alternativa.setQuestao(questao);

            alternativaRepository.save(alternativa);

        });

    }

    @Transactional
    public void deletarPorQuestao(Long questaoId) {

        alternativaRepository.deleteByQuestaoId(questaoId);

    }


    private AlternativaDTO converter(AlternativaEntity entity){

        AlternativaDTO dto = new AlternativaDTO();

        dto.setId(entity.getId());
        dto.setTexto(entity.getTexto());
        dto.setCorreta(entity.isCorreta());

        return dto;
    }
}
