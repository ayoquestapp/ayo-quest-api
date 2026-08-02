package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.dto.*;
import br.com.ayo_quest.ayo_quest.dto.resolver.AlternativaResolverDTO;
import br.com.ayo_quest.ayo_quest.dto.resolver.ModuloResolverDTO;
import br.com.ayo_quest.ayo_quest.dto.resolver.QuestaoResolverDTO;
import br.com.ayo_quest.ayo_quest.models.*;
import br.com.ayo_quest.ayo_quest.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuloService {


    private final ModuloRepository moduloRepository;
    private final ModuloImpl moduloImpl;
    private final QuestaoRepository questaoRepository;
    private final ConteudoRepository conteudoRepository;
    private final AlternativaRepository alternativaRepository;
    private final TrilhaRepository trilhaRepository;
    private final QuestaoImpl questaoImpl;
    private final ConteudoImpl conteudoImpl;
    private final AlternativaImpl alternativaImpl;
    private final ConteudoService conteudoService;
    private final QuestaoService questaoService;


    public List<ModuloDTO> listar() {
        return moduloImpl.getAllModulos();
    }

    @Transactional
    public ModuloResponseDTO criarModulo(ModuloCadastroDTO dto) {


        ModuloEntity modulo = new ModuloEntity();

        modulo.setNome(dto.getNome());
        modulo.setDescricao(dto.getDescricao());
        modulo.setCargaHoraria(dto.getCargaHoraria());
        modulo.setXpAoConcluir(dto.getXpAoConcluir());
        modulo.setTempoMaximo(dto.getTempoMaximo());


        if (dto.getTrilhaId() != null) {
            modulo.setTrilhaId(dto.getTrilhaId());
        }


        ModuloEntity moduloSalvo =
                moduloRepository.save(modulo);


        if (dto.getConteudos() != null) {

            conteudoService.salvarLista(
                    moduloSalvo,
                    dto.getConteudos()
            );

        }


        if (dto.getQuestoes() != null) {

            questaoService.salvarLista(
                    moduloSalvo,
                    dto.getQuestoes()
            );

        }


        return buscarPorId(moduloSalvo.getId());
    }


    public ModuloResponseDTO buscarPorId(Long id) {

        ModuloEntity modulo = moduloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo não encontrado"));

        ModuloResponseDTO dto = new ModuloResponseDTO();

        dto.setId(modulo.getId());
        dto.setNome(modulo.getNome());
        dto.setDescricao(modulo.getDescricao());
        dto.setCargaHoraria(modulo.getCargaHoraria());
        dto.setXpAoConcluir(modulo.getXpAoConcluir());
        dto.setTempoMaximo(modulo.getTempoMaximo());
        dto.setConteudos(conteudoService.buscarPorModulo(id));
        dto.setQuestoes(questaoService.buscarPorModulo(id));

        return dto;
    }

    public

    @Transactional
    ModuloResponseDTO atualizarModulo(Long id, ModuloAtualizacaoDTO dto) {

        ModuloEntity modulo = moduloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo não encontrado"));

        modulo.setNome(dto.getNome());
        modulo.setDescricao(dto.getDescricao());
        modulo.setCargaHoraria(dto.getCargaHoraria());
        modulo.setXpAoConcluir(dto.getXpAoConcluir());
        modulo.setTempoMaximo(dto.getTempoMaximo());

        if (dto.getTrilhaId() != null) {
            modulo.setTrilhaId(dto.getTrilhaId());
        }

        moduloRepository.save(modulo);

        conteudoService.atualizarLista(
                modulo,
                dto.getConteudos()
        );

        questaoService.atualizarLista(
                modulo,
                dto.getQuestoes()
        );

        return buscarPorId(id);
    }

    @Transactional
    public void deletarModulo(Long id) {

        if (!moduloRepository.existsById(id)) {
            throw new RuntimeException("Módulo não encontrado");
        }

        questaoService.deletarPorModulo(id);

        conteudoService.deletarPorModulo(id);

        moduloRepository.deleteById(id);
    }

    public ModuloResolverDTO buscarModuloResolver(Long id) {

        ModuloResolverDTO modulo = moduloImpl.buscarModuloResolver(id);

        modulo.setConteudos(conteudoImpl.buscarConteudosPorModulo(id));

        List<QuestaoResolverDTO> questoes =
                questaoImpl.buscarQuestoesParaValidacao(id);

        for (QuestaoResolverDTO questao : questoes) {

            questao.setAlternativas(
                    alternativaImpl.buscarAlternativasPorQuestao(questao.getId())
            );
        }

        modulo.setQuestoes(questoes);

        return modulo;
    }

    public ResultadoModuloDTO conferirRespostas(
            Long id,
            Map<String, Object> respostas) {

        return moduloImpl.conferirRespostas(id, respostas);
    }

    public List<ModuloDTO> buscarPorTrilha(Long id) {
        return moduloRepository.buscarPorTrilha(id);
    }
}