package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.dto.AlternativaDTO;
import br.com.ayo_quest.ayo_quest.dto.QuestaoDTO;
import br.com.ayo_quest.ayo_quest.dto.ResultadoModuloDTO;
import br.com.ayo_quest.ayo_quest.enuns.TipoQuestao;
import br.com.ayo_quest.ayo_quest.models.ModuloEntity;
import br.com.ayo_quest.ayo_quest.repository.ModuloRepository;
import br.com.ayo_quest.ayo_quest.repository.QuestaoImpl;
import br.com.ayo_quest.ayo_quest.service.TentativaModuloService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CorrigirModuloService {

    private final QuestaoImpl questaoImpl;

    private final TentativaModuloService tentativaService;

    private final ModuloRepository moduloRepository;

    @Transactional
    public ResultadoModuloDTO corrigir(
            Long moduloId,
            Long tentativaId,
            UUID profileId,
            Map<String, Object> respostas
    ) {

        System.out.println();
        System.out.println("==============================================");
        System.out.println("          INICIANDO CORREÇÃO DO MÓDULO");
        System.out.println("==============================================");

        System.out.println("MÓDULO ID: " + moduloId);
        System.out.println("TENTATIVA ID: " + tentativaId);
        System.out.println("PROFILE ID: " + profileId);
        System.out.println("RESPOSTAS RECEBIDAS: " + respostas);


        List<QuestaoDTO> questoes =
                questaoImpl.buscarQuestoesPorModulo(moduloId);

        if (questoes == null) {

            questoes = Collections.emptyList();

        }

        System.out.println();
        System.out.println("TOTAL DE QUESTÕES ENCONTRADAS: " + questoes.size());

        int acertos = 0;

        int xp = 0;


        for (QuestaoDTO questao : questoes) {

            System.out.println();
            System.out.println("----------------------------------------------");
            System.out.println("QUESTÃO ID: " + questao.getId());
            System.out.println("TIPO: " + questao.getTipo());
            System.out.println("XP: " + questao.getXp());


            Object resposta =
                    respostas.get(
                            String.valueOf(
                                    questao.getId()
                            )
                    );

            System.out.println("RESPOSTA RECEBIDA: " + resposta);

            if (resposta == null) {

                System.out.println(">>> QUESTÃO NÃO RESPONDIDA");

                continue;
            }

            if (questao.getAlternativas() == null) {

                System.out.println(">>> ALTERNATIVAS: NULL");

            } else {

                System.out.println(
                        "TOTAL DE ALTERNATIVAS: "
                                + questao.getAlternativas().size()
                );

                for (AlternativaDTO alternativa :
                        questao.getAlternativas()) {

                    System.out.println(
                            "ALTERNATIVA -> "
                                    + "ID: " + alternativa.getId()
                                    + " | CORRETA: " + alternativa.isCorreta()
                                    + " | TEXTO: " + alternativa.getTexto()
                    );
                }
            }

            boolean acertou =
                    validarResposta(
                            questao,
                            resposta
                    );


            System.out.println(
                    ">>> RESULTADO DA QUESTÃO: "
                            + (acertou ? "ACERTO" : "ERRO")
            );

            if (acertou) {

                acertos++;

                xp += questao.getXp();

            }
        }

        int total =
                questoes.size();


        int erros =
                total - acertos;


        double nota =
                total == 0
                        ? 0
                        : ((double) acertos / total) * 100;

        ModuloEntity modulo =
                moduloRepository.findById(moduloId)
                        .orElseThrow(() ->
                                new RuntimeException("Módulo não encontrado")
                        );

        double notaMinima = modulo.getNotaMinima();

        boolean aprovado = nota >= notaMinima;


        System.out.println();
        System.out.println("==============================================");
        System.out.println("              RESULTADO FINAL");
        System.out.println("==============================================");

        System.out.println("TOTAL: " + total);
        System.out.println("ACERTOS: " + acertos);
        System.out.println("ERROS: " + erros);
        System.out.println("NOTA: " + nota);
        System.out.println("XP GANHO: " + xp);


        ResultadoModuloDTO resultado =
                ResultadoModuloDTO.builder()

                        .totalQuestoes(
                                (long) total
                        )

                        .acertos(
                                acertos
                        )

                        .erros(
                                erros
                        )

                        .nota(
                                nota
                        )



                        .xpGanho(
                                xp
                        )

                        .aprovado(aprovado)

                        .build();


        tentativaService.concluirTentativa(
                tentativaId,
                profileId,
                resultado
        );


        System.out.println("==============================================");
        System.out.println("          CORREÇÃO FINALIZADA");
        System.out.println("==============================================");
        System.out.println();


        return resultado;
    }

    private boolean validarResposta(
            QuestaoDTO questao,
            Object resposta
    ) {

        System.out.println();
        System.out.println("VALIDANDO RESPOSTA...");
        System.out.println("TIPO DA QUESTÃO: " + questao.getTipo());
        System.out.println("RESPOSTA: " + resposta);


        if (resposta == null) {

            System.out.println(
                    ">>> RESPOSTA NULL"
            );

            return false;
        }


        if (questao.getTipo() == null) {

            System.out.println(
                    ">>> TIPO DA QUESTÃO NULL"
            );

            return false;
        }


        if (questao.getAlternativas() == null) {

            System.out.println(
                    ">>> ALTERNATIVAS NULL"
            );

            return false;
        }

        if (questao.getTipo() == TipoQuestao.MULTIPLA_ESCOLHA) {

            return validarAlternativaUnica(
                    questao,
                    resposta
            );
        }

        if (questao.getTipo() == TipoQuestao.VERDADEIRO_FALSO) {

            return validarAlternativaUnica(
                    questao,
                    resposta
            );
        }

        if (questao.getTipo() == TipoQuestao.CAIXAS_SELECAO) {

            return validarCaixasSelecao(
                    questao,
                    resposta
            );
        }

        if (questao.getTipo() == TipoQuestao.QUESTAO_ABERTA) {

            return validarQuestaoAberta(
                    questao,
                    resposta
            );
        }


        System.out.println(
                ">>> TIPO DE QUESTÃO NÃO SUPORTADO: "
                        + questao.getTipo()
        );

        return false;
    }


    private boolean validarAlternativaUnica(
            QuestaoDTO questao,
            Object resposta
    ) {

        Long alternativaSelecionada;

        try {

            alternativaSelecionada =
                    Long.valueOf(
                            resposta.toString()
                    );

        } catch (NumberFormatException e) {

            System.out.println(
                    ">>> ERRO AO CONVERTER RESPOSTA PARA LONG: "
                            + resposta
            );

            return false;
        }


        System.out.println(
                "ALTERNATIVA SELECIONADA: "
                        + alternativaSelecionada
        );

        for (AlternativaDTO alternativa :
                questao.getAlternativas()) {

            System.out.println(
                    "COMPARANDO -> "
                            + "ID: " + alternativa.getId()
                            + " | SELECIONADA: "
                            + alternativaSelecionada
                            + " | CORRETA: "
                            + alternativa.isCorreta()
            );


            if (
                    Objects.equals(
                            alternativa.getId(),
                            alternativaSelecionada
                    )
                            &&
                            alternativa.isCorreta()
            ) {

                System.out.println(
                        ">>> ALTERNATIVA CORRETA!"
                );

                return true;
            }
        }


        System.out.println(
                ">>> ALTERNATIVA INCORRETA!"
        );

        return false;
    }


    private boolean validarCaixasSelecao(
            QuestaoDTO questao,
            Object resposta
    ) {

        if (!(resposta instanceof Map<?, ?> mapa)) {

            System.out.println(
                    ">>> RESPOSTA DE CAIXAS_SELECAO NÃO É UM MAP"
            );

            System.out.println(
                    "TIPO RECEBIDO: "
                            + resposta.getClass()
            );

            return false;
        }

        Set<Long> escolhidas =
                mapa.entrySet()
                        .stream()

                        .filter(entry ->
                                Boolean.TRUE.equals(
                                        entry.getValue()
                                )
                        )

                        .map(entry ->
                                Long.valueOf(
                                        entry.getKey()
                                                .toString()
                                )
                        )

                        .collect(
                                Collectors.toSet()
                        );


        Set<Long> corretas =
                questao.getAlternativas()
                        .stream()

                        .filter(
                                AlternativaDTO::isCorreta
                        )

                        .map(
                                AlternativaDTO::getId
                        )

                        .collect(
                                Collectors.toSet()
                        );


        System.out.println(
                "ALTERNATIVAS ESCOLHIDAS: "
                        + escolhidas
        );

        System.out.println(
                "ALTERNATIVAS CORRETAS: "
                        + corretas
        );

        boolean acertou =
                escolhidas.equals(
                        corretas
                );


        System.out.println(
                "CAIXAS SELEÇÃO -> "
                        + (acertou
                        ? "CORRETA"
                        : "INCORRETA")
        );


        return acertou;
    }

    private boolean validarQuestaoAberta(
            QuestaoDTO questao,
            Object resposta
    ) {

        System.out.println(
                ">>> QUESTÃO ABERTA: "
                        + "CORREÇÃO AUTOMÁTICA NÃO IMPLEMENTADA"
        );

        return false;
    }
}