package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.dto.AlternativaDTO;
import br.com.ayo_quest.ayo_quest.dto.QuestaoDTO;
import br.com.ayo_quest.ayo_quest.dto.ResultadoModuloDTO;
import br.com.ayo_quest.ayo_quest.repository.QuestaoImpl;
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



    @Transactional
    public ResultadoModuloDTO corrigir(

            Long moduloId,

            Long tentativaId,

            UUID profileId,

            Map<String,Object> respostas

    ){


        List<QuestaoDTO> questoes =
                questaoImpl.buscarQuestoesPorModulo(moduloId);



        int acertos = 0;

        int xp = 0;



        for(QuestaoDTO questao : questoes){


            Object resposta =
                    respostas.get(
                            String.valueOf(
                                    questao.getId()
                            )
                    );



            if(resposta == null){
                continue;
            }



            boolean acertou =
                    validarResposta(
                            questao,
                            resposta
                    );



            if(acertou){

                acertos++;

                xp += questao.getXp();

            }

        }



        int total =
                questoes.size();



        double nota =
                total == 0
                        ?
                        0
                        :
                        ((double)acertos / total) * 100;



        ResultadoModuloDTO resultado =
                ResultadoModuloDTO.builder()

                        .totalQuestoes((long) total)

                        .acertos(acertos)

                        .erros(total-acertos)

                        .nota(nota)

                        .xpGanho(xp)

                        .build();



        tentativaService.concluirTentativa(
                tentativaId,
                profileId,
                resultado
        );



        return resultado;

    }




    private boolean validarResposta(
            QuestaoDTO questao,
            Object resposta
    ) {


        switch (questao.getTipo()) {


            case VERDADEIRO_FALSO:
            case MULTIPLA_ESCOLHA:


                if(resposta == null){
                    return false;
                }


                Long alternativaSelecionada =
                        Long.valueOf(resposta.toString());



                return Optional.ofNullable(
                                questao.getAlternativas()
                        )
                        .orElse(Collections.emptyList())
                        .stream()
                        .anyMatch(alt ->
                                alt.getId()
                                        .equals(alternativaSelecionada)
                                        &&
                                        alt.isCorreta()
                        );




            case CAIXAS_SELECAO:


                if(resposta == null){
                    return false;
                }


                Map<String,Object> selecionadas =
                        (Map<String,Object>) resposta;



                Set<Long> escolhidas =
                        selecionadas.entrySet()
                                .stream()
                                .filter(e ->
                                        Boolean.TRUE.equals(e.getValue())
                                )
                                .map(e ->
                                        Long.valueOf(e.getKey())
                                )
                                .collect(Collectors.toSet());



                Set<Long> corretas =
                        Optional.ofNullable(
                                        questao.getAlternativas()
                                )
                                .orElse(Collections.emptyList())
                                .stream()
                                .filter(AlternativaDTO::isCorreta)
                                .map(AlternativaDTO::getId)
                                .collect(Collectors.toSet());



                return escolhidas.equals(corretas);



            case QUESTAO_ABERTA:

                return false;


            default:

                return false;
        }

    }

}
