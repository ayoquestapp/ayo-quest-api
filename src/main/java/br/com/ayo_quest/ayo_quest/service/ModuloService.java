package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.dto.*;
import br.com.ayo_quest.ayo_quest.dto.resolver.AlternativaResolverDTO;
import br.com.ayo_quest.ayo_quest.dto.resolver.ConteudoResolverDTO;
import br.com.ayo_quest.ayo_quest.dto.resolver.ModuloResolverDTO;
import br.com.ayo_quest.ayo_quest.dto.resolver.QuestaoResolverDTO;
import br.com.ayo_quest.ayo_quest.enuns.TipoQuestao;
import br.com.ayo_quest.ayo_quest.models.*;
import br.com.ayo_quest.ayo_quest.repository.ModuloRepository;
import br.com.ayo_quest.ayo_quest.repository.TrilhaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuloService {

    @Autowired
    private TrilhaRepository trilhaRepository;

    @Autowired
    private ModuloRepository repository;

    @Transactional
    public ModuloEntity salvar(ModuloCadastroDTO dto) {

        ModuloEntity modulo = new ModuloEntity();

        modulo.setNome(dto.getNome());
        modulo.setDescricao(dto.getDescricao());
        modulo.setCargaHoraria(dto.getCargaHoraria());
        modulo.setXpAoConcluir(dto.getXpAoConcluir());

        if (dto.getTrilhaId() != null) {

            TrilhaEntity trilha = trilhaRepository.findById(dto.getTrilhaId())
                    .orElseThrow(() ->
                            new RuntimeException("Trilha não encontrada"));

            modulo.setTrilha(trilha);
        }

        if (dto.getConteudos() != null && !dto.getConteudos().isEmpty()) {

            List<ConteudoEntity> conteudos = dto.getConteudos()
                    .stream()
                    .map(c -> {

                        ConteudoEntity conteudo = new ConteudoEntity();

                        conteudo.setTipo(c.getTipo());
                        conteudo.setTitulo(c.getTitulo());
                        conteudo.setValor(c.getValor());
                        conteudo.setModulo(modulo);

                        return conteudo;

                    })
                    .collect(Collectors.toList());

            modulo.setConteudos(conteudos);
        }

        // Questões
        if (dto.getQuestoes() != null && !dto.getQuestoes().isEmpty()) {

            List<QuestaoEntity> questoes = dto.getQuestoes()
                    .stream()
                    .map(q -> {

                        QuestaoEntity questao = new QuestaoEntity();

                        questao.setEnunciado(q.getEnunciado());
                        questao.setTipo(q.getTipo());
                        questao.setXp(q.getXp());
                        questao.setModulo(modulo);

                        if (q.getAlternativas() != null && !q.getAlternativas().isEmpty()) {

                            List<AlternativaEntity> alternativas = q.getAlternativas()
                                    .stream()
                                    .map(a -> {

                                        AlternativaEntity alternativa = new AlternativaEntity();

                                        alternativa.setTexto(a.getTexto());
                                        alternativa.setCorreta(a.isCorreta());

                                        // relacionamento
                                        alternativa.setQuestao(questao);

                                        return alternativa;

                                    })
                                    .collect(Collectors.toList());

                            questao.setAlternativas(alternativas);
                        }

                        return questao;

                    })
                    .collect(Collectors.toList());

            modulo.setQuestoes(questoes);
        }

        return repository.save(modulo);
    }

    public List<ModuloDTO> listar() {

        List<ModuloEntity> modulos = repository.listarComTrilha();

        return modulos.stream().map(modulo -> {

            TrilhaResumoDTO trilhaDTO = null;

            if (modulo.getTrilha() != null) {
                trilhaDTO = new TrilhaResumoDTO(
                        modulo.getTrilha().getId(),
                        modulo.getTrilha().getNome()
                );
            }

            List<QuestaoDTO> questoes = modulo.getQuestoes()
                    .stream()
                    .map(q -> {
                        QuestaoDTO dto = new QuestaoDTO();
                        dto.setEnunciado(q.getEnunciado());
                        dto.setTipo(q.getTipo());
                        dto.setXp(q.getXp());

                        dto.setAlternativas(
                                q.getAlternativas()
                                        .stream()
                                        .map(a -> {
                                            AlternativaDTO alt = new AlternativaDTO();
                                            alt.setTexto(a.getTexto());
                                            alt.setCorreta(a.isCorreta());
                                            return alt;
                                        })
                                        .toList()
                        );

                        return dto;
                    })
                    .toList();

            return new ModuloDTO(
                    modulo.getId(),
                    modulo.getNome(),
                    modulo.getDescricao(),
                    modulo.getCargaHoraria(),
                    modulo.getXpAoConcluir(),
                    trilhaDTO,
                    questoes
            );

        }).toList();
    }

    @Transactional
    public void deletarModulo(Long id) {

        ModuloEntity modulo = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Módulo não encontrado")
                );

        repository.delete(modulo);
    }

    @Transactional
    public ModuloDTO atualizar(Long id, ModuloAtualizacaoDTO dto) {

        ModuloEntity modulo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo não encontrado"));


        // Dados básicos
        modulo.setNome(dto.getNome());
        modulo.setDescricao(dto.getDescricao());
        modulo.setCargaHoraria(dto.getCargaHoraria());
        modulo.setXpAoConcluir(dto.getXpAoConcluir());


        // Atualiza trilha
        if (dto.getTrilhaId() != null) {

            TrilhaEntity trilha = trilhaRepository.findById(dto.getTrilhaId())
                    .orElseThrow(() -> new RuntimeException("Trilha não encontrada"));

            modulo.setTrilha(trilha);
        }


        // Atualiza conteúdos
        if (dto.getConteudos() != null) {

            modulo.getConteudos().clear();

            List<ConteudoEntity> conteudos = dto.getConteudos()
                    .stream()
                    .map(c -> {

                        ConteudoEntity conteudo = new ConteudoEntity();

                        conteudo.setTipo(c.getTipo());
                        conteudo.setTitulo(c.getTitulo());
                        conteudo.setValor(c.getValor());
                        conteudo.setModulo(modulo);

                        return conteudo;

                    })
                    .collect(Collectors.toList());


            modulo.getConteudos().addAll(conteudos);
        }


        // Atualiza questões
        if (dto.getQuestoes() != null) {

            modulo.getQuestoes().clear();


            List<QuestaoEntity> questoes = dto.getQuestoes()
                    .stream()
                    .map(q -> {


                        QuestaoEntity questao = new QuestaoEntity();

                        questao.setTipo(q.getTipo());
                        questao.setEnunciado(q.getEnunciado());
                        questao.setXp(q.getXp());
                        questao.setModulo(modulo);


                        if (q.getAlternativas() != null) {


                            List<AlternativaEntity> alternativas =
                                    q.getAlternativas()
                                            .stream()
                                            .map(a -> {


                                                AlternativaEntity alternativa =
                                                        new AlternativaEntity();


                                                alternativa.setTexto(a.getTexto());
                                                alternativa.setCorreta(a.isCorreta());
                                                alternativa.setQuestao(questao);


                                                return alternativa;


                                            })
                                            .collect(Collectors.toList());


                            questao.setAlternativas(alternativas);
                        }


                        return questao;


                    })
                    .collect(Collectors.toList());


            modulo.getQuestoes().addAll(questoes);
        }


        ModuloEntity salvo = repository.save(modulo);


        return ModuloDTO.builder()

                .id(salvo.getId())

                .nome(salvo.getNome())

                .descricao(salvo.getDescricao())

                .cargaHoraria(salvo.getCargaHoraria())

                .xpAoConcluir(salvo.getXpAoConcluir())


                .trilha(
                        salvo.getTrilha() != null
                                ?
                                TrilhaResumoDTO.builder()
                                        .id(salvo.getTrilha().getId())
                                        .nome(salvo.getTrilha().getNome())
                                        .build()
                                :
                                null
                )


                .questoes(
                        converterQuestoes(salvo.getQuestoes())
                )


                .build();
    }

    public ModuloResponseDTO buscarPorId(Long id) {

        ModuloEntity modulo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo não encontrado"));


        ModuloResponseDTO dto = new ModuloResponseDTO();

        dto.setId(modulo.getId());
        dto.setNome(modulo.getNome());
        dto.setDescricao(modulo.getDescricao());
        dto.setCargaHoraria(modulo.getCargaHoraria());
        dto.setXpAoConcluir(modulo.getXpAoConcluir());


        dto.setQuestoes(
                modulo.getQuestoes()
                        .stream()
                        .map(q -> {

                            QuestaoDTO questao = new QuestaoDTO();

                            questao.setTipo(q.getTipo());
                            questao.setTipoDescricao(q.getTipo().getDescricao());
                            questao.setEnunciado(q.getEnunciado());
                            questao.setXp(q.getXp());
                            questao.setId(q.getId());

                            questao.setAlternativas(
                                    q.getAlternativas()
                                            .stream()
                                            .map(a -> {

                                                AlternativaDTO alternativa = new AlternativaDTO();

                                                alternativa.setId(a.getId());
                                                alternativa.setTexto(a.getTexto());
                                                alternativa.setCorreta(a.isCorreta());

                                                return alternativa;

                                            })
                                            .toList()
                            );

                            return questao;

                        })
                        .toList()
        );
        dto.setConteudos(
                modulo.getConteudos()
                        .stream()
                        .map(c -> {

                            ConteudoDTO conteudo = new ConteudoDTO();

                            conteudo.setId(c.getId());
                            conteudo.setTipo(c.getTipo());
                            conteudo.setTitulo(c.getTitulo());
                            conteudo.setValor(c.getValor());

                            return conteudo;

                        })
                        .toList()
        );


        return dto;
    }

    public List<ModuloDTO> buscarPorTrilha(Long id) {

        return repository
                .findByTrilhaId(id)
                .stream()
                .map(m -> new ModuloDTO(
                        m.getId(),
                        m.getNome(),
                        m.getDescricao(),
                        m.getCargaHoraria(),
                        m.getXpAoConcluir(),
                        new TrilhaResumoDTO(
                                m.getTrilha().getId(),
                                m.getTrilha().getNome()
                        ),
                        converterQuestoes(m.getQuestoes())
                ))
                .toList();

    }

    public ModuloResolverDTO buscarModuloResolver(Long id) {

        ModuloEntity modulo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo não encontrado"));

        List<ConteudoResolverDTO> conteudos = modulo.getConteudos()
                .stream()
                .map(c -> new ConteudoResolverDTO(
                        c.getId(),
                        c.getTipo(),
                        c.getValor()
                ))
                .toList();

        List<QuestaoResolverDTO> questoes = modulo.getQuestoes()
                .stream()
                .map(q -> {

                    List<AlternativaResolverDTO> alternativas =
                            q.getAlternativas()
                                    .stream()
                                    .map(a -> new AlternativaResolverDTO(
                                            a.getId(),
                                            a.getTexto()
                                    ))
                                    .toList();

                    return new QuestaoResolverDTO(
                            q.getId(),
                            q.getEnunciado(),
                            q.getTipo(),
                            q.getXp(),
                            alternativas
                    );

                })
                .toList();

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

    private List<QuestaoDTO> converterQuestoes(List<QuestaoEntity> questoes) {

        return questoes.stream()
                .map(q -> {

                    QuestaoDTO dto = new QuestaoDTO();
                    dto.setEnunciado(q.getEnunciado());
                    dto.setTipo(q.getTipo());
                    dto.setXp(q.getXp());

                    dto.setAlternativas(
                            q.getAlternativas()
                                    .stream()
                                    .map(a -> {
                                        AlternativaDTO alt = new AlternativaDTO();

                                        alt.setId(a.getId());
                                        alt.setTexto(a.getTexto());
                                        alt.setCorreta(a.isCorreta());

                                        return alt;
                                    })
                                    .toList()
                    );

                    return dto;
                })
                .toList();
    }

    public ResultadoModuloDTO conferirRespostas(
            Long moduloId,
            Map<String,Object> respostasEnviadas
    ){

        ModuloEntity modulo = repository.findById(moduloId)
                .orElseThrow(() ->
                        new RuntimeException("Módulo não encontrado")
                );


        int acertos = 0;

        int totalQuestoes = modulo.getQuestoes().size();


        for(QuestaoEntity questao : modulo.getQuestoes()) {

            String key = String.valueOf(questao.getId());

            Object respostaUser = respostasEnviadas.get(key);

            boolean acertou = validarResposta(questao, respostaUser);


            System.out.println("==============================");
            System.out.println("QUESTÃO: " + questao.getId());
            System.out.println("TIPO: " + questao.getTipo());
            System.out.println("RESPOSTA USUARIO: " + respostaUser);
            System.out.println("RESULTADO: " + acertou);


            if(!acertou){

                System.out.println("ALTERNATIVAS DO BANCO:");

                questao.getAlternativas()
                        .forEach(a ->
                                System.out.println(
                                        "ID: " + a.getId()
                                                + " TEXTO: " + a.getTexto()
                                                + " CORRETA: " + a.isCorreta()
                                )
                        );
            }


            if(acertou){
                acertos++;
            }
        }


        double nota = totalQuestoes > 0
                ? ((double) acertos / totalQuestoes) * 10
                : 0;


        boolean aprovado = nota >= 7;


        int xpGanho = aprovado
                ? modulo.getXpAoConcluir().intValue()
                : (int)(modulo.getXpAoConcluir()*0.1);



        ResultadoModuloDTO resultado = new ResultadoModuloDTO();

        resultado.setAcertos(acertos);
        resultado.setErros(totalQuestoes-acertos);
        resultado.setXpGanho(xpGanho);
        resultado.setNota(nota);
        resultado.setAprovado(aprovado);


        return resultado;
    }

    private boolean validarResposta(QuestaoEntity questao, Object respostaUser) {

        if (respostaUser == null) {
            return false;
        }


        if (questao.getTipo() == TipoQuestao.MULTIPLA_ESCOLHA) {

            return questao.getAlternativas()
                    .stream()
                    .anyMatch(a ->
                            a.isCorreta()
                                    &&
                                    String.valueOf(a.getId())
                                            .equals(String.valueOf(respostaUser))
                    );
        }

        if (questao.getTipo() == TipoQuestao.VERDADEIRO_FALSO) {

            boolean resposta =
                    Boolean.parseBoolean(
                            String.valueOf(respostaUser)
                    );


            return questao.getAlternativas()
                    .stream()
                    .anyMatch(a ->
                            a.isCorreta()
                                    &&
                                    (
                                            resposta && a.getTexto().trim().equalsIgnoreCase("Verdadeiro")
                                                    ||
                                                    !resposta && a.getTexto().trim().equalsIgnoreCase("Falso")
                                    )
                    );
        }


        if (questao.getTipo() == TipoQuestao.CAIXAS_SELECAO) {


            if (!(respostaUser instanceof Map)) {
                return false;
            }


            Map<String, Boolean> respostas =
                    (Map<String, Boolean>) respostaUser;

            java.util.Set<String> respostasMarcadasComoCorretas = respostas.entrySet()
                .stream()
                .filter(entry -> Boolean.TRUE.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(java.util.stream.Collectors.toSet());

            java.util.Set<String> alternativasCorretas = questao.getAlternativas()
                    .stream()
                    .filter(AlternativaEntity::isCorreta)
                    .map(a -> String.valueOf(a.getId()))
                    .collect(java.util.stream.Collectors.toSet());

            return alternativasCorretas.equals(respostasMarcadasComoCorretas);
        }

        if (questao.getTipo() == TipoQuestao.QUESTAO_ABERTA) {


            return questao.getAlternativas()
                    .stream()
                    .filter(AlternativaEntity::isCorreta)
                    .anyMatch(a ->
                            a.getTexto()
                                    .equalsIgnoreCase(
                                            String.valueOf(respostaUser)
                                                    .trim()
                                    )
                    );

        }

        return false;
    }
}
