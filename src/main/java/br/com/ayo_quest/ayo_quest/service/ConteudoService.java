package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.dto.ConteudoDTO;
import br.com.ayo_quest.ayo_quest.models.ConteudoEntity;
import br.com.ayo_quest.ayo_quest.models.ModuloEntity;
import br.com.ayo_quest.ayo_quest.repository.ConteudoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConteudoService {

    private final ConteudoRepository conteudoRepository;


    public List<ConteudoDTO> buscarPorModulo(Long moduloId){

        return conteudoRepository.findByModuloId(moduloId)
                .stream()
                .map(this::converter)
                .toList();
    }

    public void salvarLista(
            ModuloEntity modulo,
            List<ConteudoDTO> conteudos
    ){

        conteudos.forEach(dto -> {


            ConteudoEntity conteudo = new ConteudoEntity();

            conteudo.setTitulo(dto.getTitulo());
            conteudo.setTipo(dto.getTipo());
            conteudo.setValor(dto.getValor());

            conteudo.setModulo(modulo);


            conteudoRepository.save(conteudo);

        });

    }

    @Transactional
    public void atualizarLista(
            ModuloEntity modulo,
            List<ConteudoDTO> conteudosDTO
    ) {

        if (conteudosDTO == null) {
            conteudosDTO = List.of();
        }

        List<ConteudoEntity> existentes =
                conteudoRepository.findByModuloId(modulo.getId());

        Map<Long, ConteudoEntity> existentesMap =
                existentes.stream()
                        .filter(c -> c.getId() != null)
                        .collect(Collectors.toMap(
                                ConteudoEntity::getId,
                                Function.identity()
                        ));

        Set<Long> idsRecebidos =
                conteudosDTO.stream()
                        .map(ConteudoDTO::getId)
                        .filter(id -> id != null)
                        .collect(Collectors.toSet());

        List<Long> idsRemover =
                existentes.stream()
                        .map(ConteudoEntity::getId)
                        .filter(id -> !idsRecebidos.contains(id))
                        .toList();

        if (!idsRemover.isEmpty()) {
            conteudoRepository.deleteByIdIn(idsRemover);
        }

        List<ConteudoEntity> salvar =
                conteudosDTO.stream()
                        .map(dto -> {

                            ConteudoEntity conteudo;

                            if (dto.getId() != null &&
                                    existentesMap.containsKey(dto.getId())) {

                                conteudo =
                                        existentesMap.get(dto.getId());

                            } else {

                                conteudo = new ConteudoEntity();
                                conteudo.setModulo(modulo);
                            }

                            conteudo.setTitulo(dto.getTitulo());
                            conteudo.setTipo(dto.getTipo());
                            conteudo.setValor(dto.getValor());

                            return conteudo;

                        })
                        .toList();

        conteudoRepository.saveAll(salvar);
    }

    @Transactional
    public void deletarPorModulo(Long moduloId) {

        conteudoRepository.deleteByModuloId(moduloId);

    }

    private ConteudoDTO converter(ConteudoEntity entity){

        ConteudoDTO dto = new ConteudoDTO();

        dto.setId(entity.getId());
        dto.setTitulo(entity.getTitulo());
        dto.setTipo(entity.getTipo());
        dto.setValor(entity.getValor());

        return dto;
    }
}