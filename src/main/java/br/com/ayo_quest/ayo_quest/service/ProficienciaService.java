package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.dto.proficiencia.CadastrarProficienciaDTO;
import br.com.ayo_quest.ayo_quest.dto.proficiencia.ProficienciaDTO;
import br.com.ayo_quest.ayo_quest.models.NivelEntity;
import br.com.ayo_quest.ayo_quest.models.ProficienciaEntity;
import br.com.ayo_quest.ayo_quest.models.QuestaoEntity;
import br.com.ayo_quest.ayo_quest.repository.NivelRepository;
import br.com.ayo_quest.ayo_quest.repository.ProficienciaRepository;
import br.com.ayo_quest.ayo_quest.repository.QuestaoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProficienciaService {

    private final ProficienciaRepository proficienciaRepository;
    private final NivelRepository nivelRepository;
    private final QuestaoRepository questaoRepository;

    @Transactional
    public ProficienciaDTO cadastrar(CadastrarProficienciaDTO dto) {
        NivelEntity nivel = nivelRepository.findById(dto.getNivelId())
                .orElseThrow(() -> new RuntimeException("Nível não encontrado"));

        ProficienciaEntity proficiencia = new ProficienciaEntity();
        proficiencia.setNome(dto.getNome());
        proficiencia.setDescricao(dto.getDescricao());
        proficiencia.setNivel(nivel);

        ProficienciaEntity proficienciaSalva = proficienciaRepository.save(proficiencia);

        List<Long> questoesIds = dto.getQuestoes().stream().map(questaoDTO -> {
            QuestaoEntity questao = new QuestaoEntity();
            questao.setEnunciado(questaoDTO.getEnunciado());
            questao.setTipo(questaoDTO.getTipo());
            questao.setXp(questaoDTO.getXp());
            questao.setProficiencia(proficienciaSalva);
            // Aqui, a questão também pode pertencer a um módulo, mas o DTO não informa.
            // Deixarei nulo por enquanto, assumindo que a associação é apenas com proficiência.
            return questaoRepository.save(questao).getId();
        }).collect(Collectors.toList());

        return criarProficienciaDTOResponse(proficienciaSalva, questoesIds);
    }

    public ProficienciaDTO buscarPorId(Long id) {
        ProficienciaEntity proficiencia = proficienciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proficiência não encontrada"));

        List<Long> questoesIds = questaoRepository.findByProficienciaId(id)
                .stream()
                .map(QuestaoEntity::getId)
                .collect(Collectors.toList());

        return criarProficienciaDTOResponse(proficiencia, questoesIds);
    }

    private ProficienciaDTO criarProficienciaDTOResponse(ProficienciaEntity proficiencia, List<Long> questoesIds) {
        ProficienciaDTO dto = new ProficienciaDTO();
        dto.setId(proficiencia.getId());
        dto.setNomeNivel(proficiencia.getNivel().getNomeNivel());
        dto.setDescricao(proficiencia.getDescricao());
        dto.setNivelId(proficiencia.getNivel().getId());
        dto.setQuestoesIds(questoesIds);
        dto.setTotalQuestao(questoesIds.size());
        // notaMinima não está na entidade, então não pode ser mapeado diretamente.
        // dto.setNotaMinima(...); 
        return dto;
    }
}
