package br.com.ayo_quest.ayo_quest.service;


import br.com.ayo_quest.ayo_quest.dto.*;
import br.com.ayo_quest.ayo_quest.dto.resolver.AlternativaResolverDTO;
import br.com.ayo_quest.ayo_quest.dto.resolver.ConteudoResolverDTO;
import br.com.ayo_quest.ayo_quest.dto.resolver.ModuloResolverDTO;
import br.com.ayo_quest.ayo_quest.dto.resolver.QuestaoResolverDTO;
import br.com.ayo_quest.ayo_quest.enuns.TipoQuestao;
import br.com.ayo_quest.ayo_quest.models.*;
import br.com.ayo_quest.ayo_quest.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuloService {


    private final ModuloRepository moduloRepository;
    private final ModuloImpl moduloImpl;
    private final QuestaoRepository questaoRepository;
    private final ConteudoRepository conteudoRepository;
    private final AlternativaRepository alternativaRepository;
    private final TrilhaRepository trilhaRepository;


    @Transactional
    public ModuloResponseDTO salvar(ModuloCadastroDTO dto) {

        ModuloEntity modulo = new ModuloEntity();

        modulo.setNome(dto.getNome());
        modulo.setDescricao(dto.getDescricao());
        modulo.setCargaHoraria(dto.getCargaHoraria());
        modulo.setXpAoConcluir(dto.getXpAoConcluir());
        modulo.setTempoMaximo(dto.getTempoMaximo());


        ModuloEntity moduloSalvo = moduloRepository.save(modulo);

        if (dto.getConteudos_ids() != null) {

            List<ConteudoEntity> conteudos =
                    conteudoRepository.findAllById(dto.getConteudos_ids());

            conteudos.forEach(conteudo -> {
                conteudo.setModulo(moduloSalvo);
            });

            conteudoRepository.saveAll(conteudos);
        }


        if (dto.getQuestoes_ids() != null) {

            List<QuestaoEntity> questoes =
                    questaoRepository.findAllById(dto.getQuestoes_ids());

            questoes.forEach(questao -> {
                questao.setModulo(moduloSalvo);
            });

            questaoRepository.saveAll(questoes);
        }


        return buscarPorId(moduloSalvo.getId());
    }

    public List<ModuloDTO> listar() {
        return moduloImpl.getAllModulos();
    }

    @Transactional
    public ModuloResponseDTO atualizar(Long id, ModuloAtualizacaoDTO dto) {

        ModuloEntity modulo = moduloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo não encontrado"));

        modulo.setNome(dto.getNome());
        modulo.setDescricao(dto.getDescricao());
        modulo.setCargaHoraria(dto.getCargaHoraria());
        modulo.setXpAoConcluir(dto.getXpAoConcluir());

        if (dto.getTrilhaId() != null) {

            TrilhaEntity trilha = trilhaRepository.findById(dto.getTrilhaId())
                    .orElseThrow(() -> new RuntimeException("Trilha não encontrada"));

            modulo.setTrilhaId(dto.getTrilhaId());
        }

        moduloRepository.save(modulo);

        if (dto.getConteudos_ids() != null) {

            List<ConteudoEntity> conteudos =
                    conteudoRepository.findAllById(dto.getConteudos_ids());

            conteudos.forEach(conteudo ->
                    conteudo.setModulo(modulo)
            );

            conteudoRepository.saveAll(conteudos);
        }

        if (dto.getQuestoes_ids() != null) {

            List<QuestaoEntity> questoes =
                    questaoRepository.findAllById(dto.getQuestoes_ids());

            questoes.forEach(questao ->
                    questao.setModulo(modulo)
            );

            questaoRepository.saveAll(questoes);
        }

        return buscarPorId(id);
    }

    @Transactional
    public void deletarModulo(Long id) {
        ModuloEntity modulo = moduloRepository.findById(id).orElseThrow(() -> new RuntimeException("Módulo não encontrado"));

        List<QuestaoEntity> questoes = questaoRepository.findByModuloId(id);
        questoes.forEach(questao -> {
            alternativaRepository.deleteByQuestaoId(questao.getId());
            questaoRepository.delete(questao);
        });

        List<ConteudoEntity> conteudos = conteudoRepository.findByModuloId(id);
        conteudoRepository.deleteAll(conteudos);

        moduloRepository.delete(modulo);
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

        List<Long> conteudosIds = conteudoRepository.findByModuloId(id)
                .stream()
                .map(ConteudoEntity::getId)
                .collect(Collectors.toList());
        dto.setConteudosIds(conteudosIds);

        List<Long> questoesIds = questaoRepository.findByModuloId(id)
                .stream()
                .map(QuestaoEntity::getId)
                .collect(Collectors.toList());
        dto.setQuestoesIds(questoesIds);

        return dto;
    }


    public List<ModuloDTO> buscarPorTrilha(Long id) {
        return moduloRepository.buscarPorTrilha(id);
    }

    public ModuloResolverDTO buscarModuloResolver(Long id) {

        ModuloEntity modulo = moduloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo não encontrado"));

        List<ConteudoDTO> conteudos = moduloImpl.buscarConteudosPorModulo(id);
        List<QuestaoResolverDTO> questoes = moduloImpl.buscarQuestoesPorModulo(id);

        return new ModuloResolverDTO(
                modulo.getId(),
                modulo.getNome(),
                modulo.getDescricao(),
                modulo.getCargaHoraria(),
                modulo.getXpAoConcluir(),
                conteudos,
                questoes
        );
    }

    public ResultadoModuloDTO conferirRespostas(
            Long id,
            Map<String, Object> respostas
    ) {

        List<QuestaoResolverDTO> questoes =
                moduloImpl.buscarQuestoesParaValidacao(id);


        int acertos = 0;


        for (QuestaoResolverDTO questao : questoes) {

            Object respostaUsuario =
                    respostas.get(
                            String.valueOf(questao.getId())
                    );


            boolean acertou =
                    validarResposta(
                            questao,
                            respostaUsuario
                    );


            if (acertou) {
                acertos++;
            }
        }


        int totalQuestoes = questoes.size();


        double nota =
                totalQuestoes > 0
                        ? ((double) acertos / totalQuestoes) * 10
                        : 0;


        boolean aprovado = nota >= 7;


        ResultadoModuloDTO resultado = new ResultadoModuloDTO();

        resultado.setAcertos(acertos);
        resultado.setErros(totalQuestoes - acertos);
        resultado.setNota(nota);
        resultado.setAprovado(aprovado);


        return resultado;
    }

    private boolean validarResposta(
            QuestaoResolverDTO questao,
            Object respostaUsuario
    ) {

        if(respostaUsuario == null){
            return false;
        }


        if(questao.getTipo() == TipoQuestao.MULTIPLA_ESCOLHA){

            return questao.getAlternativas()
                    .stream()
                    .anyMatch(a ->
                            a.isCorreta()
                                    &&
                                    String.valueOf(a.getId())
                                            .equals(
                                                    String.valueOf(respostaUsuario)
                                            )
                    );
        }


        if(questao.getTipo() == TipoQuestao.VERDADEIRO_FALSO){

            boolean resposta =
                    Boolean.parseBoolean(
                            String.valueOf(respostaUsuario)
                    );


            return questao.getAlternativas()
                    .stream()
                    .anyMatch(a ->
                            a.isCorreta()
                                    &&
                                    (
                                            resposta &&
                                                    a.getTexto()
                                                            .equalsIgnoreCase("Verdadeiro")

                                                    ||

                                                    !resposta &&
                                                            a.getTexto()
                                                                    .equalsIgnoreCase("Falso")
                                    )
                    );
        }


        if(questao.getTipo() == TipoQuestao.CAIXAS_SELECAO){

            if(!(respostaUsuario instanceof Map)){
                return false;
            }


            Map<String, Boolean> respostas =
                    (Map<String, Boolean>) respostaUsuario;


            Set<String> marcadas =
                    respostas.entrySet()
                            .stream()
                            .filter(e -> Boolean.TRUE.equals(e.getValue()))
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toSet());


            Set<String> corretas =
                    questao.getAlternativas()
                            .stream()
                            .filter(AlternativaResolverDTO::isCorreta)
                            .map(a -> String.valueOf(a.getId()))
                            .collect(Collectors.toSet());


            return corretas.equals(marcadas);
        }


        if(questao.getTipo() == TipoQuestao.QUESTAO_ABERTA){

            return questao.getAlternativas()
                    .stream()
                    .filter(AlternativaResolverDTO::isCorreta)
                    .anyMatch(a ->
                            a.getTexto()
                                    .equalsIgnoreCase(
                                            String.valueOf(respostaUsuario).trim()
                                    )
                    );
        }


        return false;
    }
}