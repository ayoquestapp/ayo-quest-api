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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuloService {


    private final ModuloRepository moduloRepository;
    private final ModuloImpl moduloImpl;
    private final QuestaoImpl questaoImpl;
    private final ConteudoImpl conteudoImpl;
    private final AlternativaImpl alternativaImpl;
    private final ConteudoService conteudoService;
    private final QuestaoService questaoService;
    private final TentativaModuloService tentativaService;


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
        modulo.setNotaMinima(dto.getNotaMinima());
        modulo.setNivelId(dto.getNivelId());
//        modulo.setTempoPorQuestao(dto.getTempoPorQuestao());


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
        dto.setNotaMinima(modulo.getNotaMinima());
        dto.setNivelId(modulo.getNivelId());
        dto.setTrilhaId(modulo.getTrilhaId());
//        dto.setTempoPorQuestao(modulo.getTempoPorQuestao());
        dto.setConteudos(conteudoService.buscarPorModulo(id));
        dto.setQuestoes(questaoService.buscarPorModulo(id));

        return dto;
    }


    @Transactional
    public ModuloResponseDTO atualizarModulo(
            Long id,
            ModuloAtualizacaoDTO dto
    ) {

        long inicio = System.currentTimeMillis();

        ModuloEntity modulo = moduloRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Módulo não encontrado")
                );

        modulo.setNome(dto.getNome());
        modulo.setDescricao(dto.getDescricao());
        modulo.setCargaHoraria(dto.getCargaHoraria());
        modulo.setXpAoConcluir(dto.getXpAoConcluir());
        modulo.setNotaMinima(dto.getNotaMinima());
        modulo.setNivelId(dto.getNivelId());

        if (dto.getTrilhaId() != null) {
            modulo.setTrilhaId(dto.getTrilhaId());
        }

        conteudoService.atualizarLista(
                modulo,
                dto.getConteudos()
        );

        questaoService.atualizarLista(
                modulo,
                dto.getQuestoes()
        );

        System.out.println(
                "PUT TOTAL: "
                        + (System.currentTimeMillis() - inicio)
                        + " ms"
        );

        return montarResponse(modulo, dto);
    }

    private ModuloResponseDTO montarResponse(
            ModuloEntity modulo,
            ModuloAtualizacaoDTO dto
    ) {

        ModuloResponseDTO resposta = new ModuloResponseDTO();

        resposta.setId(modulo.getId());
        resposta.setNome(modulo.getNome());
        resposta.setDescricao(modulo.getDescricao());
        resposta.setCargaHoraria(modulo.getCargaHoraria());
        resposta.setXpAoConcluir(modulo.getXpAoConcluir());
        resposta.setNotaMinima(modulo.getNotaMinima());
        resposta.setNivelId(modulo.getNivelId());
        resposta.setTrilhaId(modulo.getTrilhaId());
        resposta.setConteudos(dto.getConteudos());
        resposta.setQuestoes(dto.getQuestoes());

        return resposta;
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

        ModuloResolverDTO modulo =
                moduloImpl.buscarModuloResolver(id);

        modulo.setConteudos(
                conteudoImpl.buscarConteudosPorModulo(id)
        );


        List<QuestaoResolverDTO> questoes =
                questaoImpl.buscarQuestoesParaValidacao(id);


        List<Long> ids =
                questoes.stream()
                        .map(QuestaoResolverDTO::getId)
                        .toList();


        List<AlternativaResolverDTO> alternativas =
                alternativaImpl.buscarAlternativasPorQuestao(ids);


        questoes.forEach(questao -> {

            List<AlternativaResolverDTO> alternativasDaQuestao =
                    alternativas.stream()
                            .filter(alternativa ->
                                    alternativa.getQuestaoId()
                                            .equals(questao.getId())
                            )
                            .toList();


            questao.setAlternativas(
                    alternativasDaQuestao
            );

        });


        modulo.setQuestoes(questoes);


        return modulo;
    }

//    @Transactional
//    public ResultadoModuloDTO conferirRespostas(
//            Long id,
//            Map<String,Object> respostas
//    ) {
//
//
//        List<QuestaoDTO> questoes =
//                questaoImpl.buscarQuestoesPorModulo(id);
//
//
//        int acertos = 0;
//        int total = questoes.size();
//
//        int xp = 0;
//
//
//        for (QuestaoDTO questao : questoes) {
//
//
//            Object resposta =
//                    respostas.get(
//                            questao.getId().toString()
//                    );
//
//
//            boolean acertou = false;
//
//
//            switch (questao.getTipo()) {
//
//
//                case VERDADEIRO_FALSO:
//                case MULTIPLA_ESCOLHA:
//
//
//                    Long alternativaSelecionada =
//                            Long.valueOf(
//                                    resposta.toString()
//                            );
//
//
//                    acertou =
//                            questao.getAlternativas()
//                                    .stream()
//                                    .anyMatch(alt ->
//                                            alt.getId()
//                                                    .equals(alternativaSelecionada)
//                                                    &&
//                                                    alt.isCorreta()
//                                    );
//
//
//                    break;
//
//
//
//                case CAIXAS_SELECAO:
//
//
//                    Map<String,Object> selecionadas =
//                            (Map<String,Object>) resposta;
//
//
//                    Set<Long> escolhidas =
//                            selecionadas.entrySet()
//                                    .stream()
//                                    .filter(e -> Boolean.TRUE.equals(e.getValue()))
//                                    .map(e -> Long.valueOf(e.getKey()))
//                                    .collect(Collectors.toSet());
//
//
//                    Set<Long> corretas =
//                            questao.getAlternativas()
//                                    .stream()
//                                    .filter(AlternativaDTO::isCorreta)
//                                    .map(AlternativaDTO::getId)
//                                    .collect(Collectors.toSet());
//
//
//                    acertou =
//                            escolhidas.equals(corretas);
//
//
//                    break;
//            }
//
//
//
//            if(acertou){
//                acertos++;
//                xp += questao.getXp();
//            }
//
//        }
//
//
//        double nota =
//                ((double) acertos / total) * 100;
//
//
//        return ResultadoModuloDTO.builder()
//                .totalQuestoes((long) total)
//                .acertos(acertos)
//                .erros(total-acertos)
//                .nota(nota)
//                .xpGanho(xp)
//                .build();
//    }

    public List<ModuloDTO> buscarPorTrilha(Long id) {
        return moduloRepository.buscarPorTrilha(id);
    }
}