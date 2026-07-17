package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.dto.tentativaModulo.EnviarRespostaDTO;
import br.com.ayo_quest.ayo_quest.dto.tentativaModulo.RespostaQuestaoDTO;
import br.com.ayo_quest.ayo_quest.dto.tentativaModulo.ResultadoRespostaDTO;
import br.com.ayo_quest.ayo_quest.models.*;
import br.com.ayo_quest.ayo_quest.repository.QuestaoRepository;
import br.com.ayo_quest.ayo_quest.repository.RespostaAlternativaRepository;
import br.com.ayo_quest.ayo_quest.repository.RespostaQuestaoRepository;
import br.com.ayo_quest.ayo_quest.repository.TentativaModuloRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RespostaService {


    private final TentativaModuloRepository tentativaRepository;

    private final QuestaoRepository questaoRepository;

    private final RespostaQuestaoRepository respostaQuestaoRepository;

    private final RespostaAlternativaRepository respostaAlternativaRepository;


    @Transactional
    public ResultadoRespostaDTO finalizar(
            EnviarRespostaDTO dto
    ) {


        TentativaModuloEntity tentativa =
                tentativaRepository.findById(dto.getTentativaId())
                        .orElseThrow(() ->
                                new RuntimeException("Tentativa não encontrada")
                        );


        int acertos = 0;

        int erros = 0;

        int xp = 0;



        for (RespostaQuestaoDTO respostaDTO : dto.getRespostas()) {


            QuestaoEntity questao =
                    questaoRepository.findById(respostaDTO.getQuestaoId())
                            .orElseThrow(() ->
                                    new RuntimeException("Questão não encontrada")
                            );


            boolean correta =
                    corrigirQuestao(
                            questao,
                            respostaDTO
                    );


            RespostaQuestaoEntity resposta =
                    new RespostaQuestaoEntity();


            resposta.setTentativa(tentativa);

            resposta.setQuestao(questao);

            resposta.setCorreta(correta);


            if(respostaDTO.getRespostaTexto() != null){

                resposta.setRespostaTexto(
                        respostaDTO.getRespostaTexto()
                );

            }


            if(correta){

                acertos++;

                xp += questao.getXp();

                resposta.setXpObtido(
                        questao.getXp()
                );

            }
            else {

                erros++;

                resposta.setXpObtido(0);

            }


            RespostaQuestaoEntity salva =
                    respostaQuestaoRepository.save(resposta);



            if(respostaDTO.getAlternativas() != null){


                for(Long alternativaId :
                        respostaDTO.getAlternativas()){


                    AlternativaEntity alternativa =
                            new AlternativaEntity();

                    alternativa.setId(alternativaId);


                    RespostaAlternativaEntity respostaAlt =
                            new RespostaAlternativaEntity();


                    respostaAlt.setRespostaQuestao(salva);

                    respostaAlt.setAlternativa(alternativa);


                    respostaAlternativaRepository.save(respostaAlt);

                }

            }

        }


        double nota =
                (acertos * 100.0)
                        /
                        dto.getRespostas().size();



        return new ResultadoRespostaDTO(
                acertos,
                erros,
                xp,
                nota
        );

    }


    private boolean corrigirQuestao(
            QuestaoEntity questao,
            RespostaQuestaoDTO resposta
    ){


        Set<Long> corretas =
                questao.getAlternativas()
                        .stream()
                        .filter(AlternativaEntity::isCorreta)
                        .map(AlternativaEntity::getId)
                        .collect(java.util.stream.Collectors.toSet());


        Set<Long> marcadas =
                new HashSet<>(
                        resposta.getAlternativas()
                );


        return corretas.equals(marcadas);

    }

}
