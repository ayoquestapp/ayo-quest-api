package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.dto.ConteudoDTO;
import br.com.ayo_quest.ayo_quest.models.ConteudoEntity;
import br.com.ayo_quest.ayo_quest.models.ModuloEntity;
import br.com.ayo_quest.ayo_quest.repository.ConteudoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

        List<ConteudoEntity> existentes =
                conteudoRepository.findByModuloId(modulo.getId());

        existentes.forEach(conteudo -> {

            boolean existe = conteudosDTO.stream()
                    .anyMatch(dto ->
                            dto.getId() != null &&
                                    dto.getId().equals(conteudo.getId()));

            if (!existe) {
                conteudoRepository.delete(conteudo);
            }

        });

        conteudosDTO.forEach(dto -> {

            ConteudoEntity conteudo =
                    dto.getId() != null
                            ? conteudoRepository.findById(dto.getId()).orElse(new ConteudoEntity())
                            : new ConteudoEntity();

            conteudo.setTitulo(dto.getTitulo());
            conteudo.setTipo(dto.getTipo());
            conteudo.setValor(dto.getValor());
            conteudo.setModulo(modulo);

            conteudoRepository.save(conteudo);

        });

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