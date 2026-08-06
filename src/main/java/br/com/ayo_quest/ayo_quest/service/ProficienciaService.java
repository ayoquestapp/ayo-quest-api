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
        proficiencia.setNotaMinima(dto.getNotaMinima());



        ProficienciaEntity proficienciaSalva = proficienciaRepository.save(proficiencia);

        List<Long> questoesIds = dto.getQuestoes().stream().map(questaoDTO -> {

            System.out.println("QUESTAO DTO:");
            System.out.println("TEMPO: " + questaoDTO.getTempoPorQuestao());

            QuestaoEntity questao = new QuestaoEntity();
            questao.setEnunciado(questaoDTO.getEnunciado());
            questao.setTipo(questaoDTO.getTipo());
            questao.setXp(questaoDTO.getXp());
            questao.setTempoPorQuestao(questaoDTO.getTempoPorQuestao());
            questao.setProficiencia(proficienciaSalva);
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

    public List<ProficienciaDTO> listarTodas() {
        return proficienciaRepository.findAll().stream()
                .map(proficiencia -> {
                    List<Long> questoesIds = questaoRepository.findByProficienciaId(proficiencia.getId())
                            .stream()
                            .map(QuestaoEntity::getId)
                            .collect(Collectors.toList());
                    return criarProficienciaDTOResponse(proficiencia, questoesIds);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ProficienciaDTO atualizar(Long id, CadastrarProficienciaDTO dto) {
        ProficienciaEntity proficiencia = proficienciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proficiência não encontrada"));

        NivelEntity nivel = nivelRepository.findById(dto.getNivelId())
                .orElseThrow(() -> new RuntimeException("Nível não encontrado"));

        proficiencia.setNome(dto.getNome());
        proficiencia.setDescricao(dto.getDescricao());
        proficiencia.setNivel(nivel);
        proficiencia.setNotaMinima(dto.getNotaMinima());

        // Por simplicidade, não vamos atualizar as questões aqui.
        // A lógica de atualização de questões pode ser complexa.

        ProficienciaEntity proficienciaAtualizada = proficienciaRepository.save(proficiencia);

        List<Long> questoesIds = questaoRepository.findByProficienciaId(id)
                .stream()
                .map(QuestaoEntity::getId)
                .collect(Collectors.toList());

        return criarProficienciaDTOResponse(proficienciaAtualizada, questoesIds);
    }

    @Transactional
    public void deletar(Long id) {
        ProficienciaEntity proficiencia = proficienciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proficiência não encontrada"));
        List<QuestaoEntity> questoes = questaoRepository.findByProficienciaId(id);
        questaoRepository.deleteAll(questoes);

        proficienciaRepository.delete(proficiencia);
    }

    private ProficienciaDTO criarProficienciaDTOResponse(ProficienciaEntity proficiencia, List<Long> questoesIds) {
        ProficienciaDTO dto = new ProficienciaDTO();
        dto.setId(proficiencia.getId());
        dto.setNomeNivel(proficiencia.getNivel().getNomeNivel());
        dto.setDescricao(proficiencia.getDescricao());
        dto.setNivelId(proficiencia.getNivel().getId());
        dto.setQuestoesIds(questoesIds);
        dto.setTotalQuestao(questoesIds.size());
       
        return dto;
    }
}