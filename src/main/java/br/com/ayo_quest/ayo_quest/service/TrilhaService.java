package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.dto.TrilhaCreateDTO;
import br.com.ayo_quest.ayo_quest.dto.TrilhaDTO;
import br.com.ayo_quest.ayo_quest.dto.TrilhaUpdateDTO;
import br.com.ayo_quest.ayo_quest.models.ModuloEntity;
import br.com.ayo_quest.ayo_quest.models.TrilhaEntity;
import br.com.ayo_quest.ayo_quest.repository.ModuloRepository;
import br.com.ayo_quest.ayo_quest.repository.TrilhaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class TrilhaService {

    @Autowired
    private TrilhaRepository trilhaRepository;

    @Autowired
    private ModuloRepository moduloRepository;

    public List<TrilhaDTO> listar(){


        List<TrilhaEntity> trilhas =
                trilhaRepository.findAll();



        return trilhas.stream()
                .map(trilha -> {


                    Long quantidadeModulos =
                            moduloRepository
                                    .contarModulosPorTrilha(trilha.getId());



                    return new TrilhaDTO(

                            trilha.getId(),

                            trilha.getNome(),

                            trilha.getCode(),

                            trilha.getDescricao(),

                            quantidadeModulos.intValue(),

                            trilha.getImagem(),

                            trilha.getTag(),

                            null,

                            null
                    );


                })
                .toList();

    }

    public TrilhaDTO detalhar(Long id) {
        TrilhaEntity trilha = trilhaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trilha não encontrada"));

        Long cargaHoraria = moduloRepository.calcularCargaHorariaPorTrilha(id);
        Long xpTotal = moduloRepository.calcularXpTotalPorTrilha(id);
        Long quantidadeModulos =
                moduloRepository
                        .contarModulosPorTrilha(trilha.getId());

        return new TrilhaDTO(
                trilha.getId(),
                trilha.getNome(),
                trilha.getCode(),
                trilha.getDescricao(),
                quantidadeModulos.intValue(),
                trilha.getImagem(),
                trilha.getTag(),
                cargaHoraria,
                xpTotal
        );
    }

    public TrilhaEntity criar(TrilhaCreateDTO dto) {

        TrilhaEntity trilha = new TrilhaEntity();

        trilha.setNome(dto.getNome());
        trilha.setDescricao(dto.getDescricao());
        trilha.setImagem(dto.getImagem());
        trilha.setCode(dto.getCode());
        trilha.setTag(dto.getTag());

        return trilhaRepository.save(trilha);
    }

    @Transactional
    public void atualizar(
            Long id,
            TrilhaUpdateDTO dto
    ){

        TrilhaEntity trilha =
                trilhaRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Trilha não encontrada")
                        );


        trilha.setNome(dto.getNome());
        trilha.setCode(dto.getCode());
        trilha.setDescricao(dto.getDescricao());
        trilha.setImagem(dto.getImagem());


        trilhaRepository.save(trilha);
    }

    public void deletar(Long id) {

        TrilhaEntity trilha =
                trilhaRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Trilha não encontrada")
                        );

        trilhaRepository.delete(trilha);
    }

    public List<TrilhaDTO> buscar(String nome){

        return trilhaRepository
                .findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(trilha -> {


                    Long quantidadeModulos =
                            moduloRepository
                                    .contarModulosPorTrilha(trilha.getId());


                    return converterDTO(
                            trilha,
                            quantidadeModulos.intValue()
                    );

                })
                .toList();

    }

    private TrilhaDTO converterDTO(
            TrilhaEntity trilha,
            Integer quantidadeModulos
    ) {

        return new TrilhaDTO(
                trilha.getId(),
                trilha.getNome(),
                trilha.getCode(),
                trilha.getDescricao(),
                quantidadeModulos,
                trilha.getImagem(),
                trilha.getTag(),
                null,
                null
        );

    }

}
