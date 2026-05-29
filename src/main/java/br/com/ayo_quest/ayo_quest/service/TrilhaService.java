package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.dto.TrilhaDTO;
import br.com.ayo_quest.ayo_quest.models.TrilhaEntity;
import br.com.ayo_quest.ayo_quest.repository.TrilhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrilhaService {

    @Autowired
    private TrilhaRepository trilhaRepository;

    public List<TrilhaDTO> listar(){
        List<TrilhaEntity> trilhas = trilhaRepository.listarComModulos();

        return trilhas.stream().map(trilha -> new TrilhaDTO(
                trilha.getId(),
                trilha.getNome(),
                trilha.getCode(),
                trilha.getDescricao(),
                trilha.getModulos().size(),
                trilha.getImagem()

        )).toList();
    }

    public TrilhaEntity detalhar(Long id) {

        return trilhaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Trilha não encontrada"));
    }

    public TrilhaEntity criar(TrilhaEntity trilha) {

        return trilhaRepository.save(trilha);
    }

    public TrilhaEntity atualizar(Long id, TrilhaEntity trilhaAtualizada) {

        TrilhaEntity trilha = detalhar(id);

        trilha.setNome(trilhaAtualizada.getNome());
        trilha.setCode(trilhaAtualizada.getCode());
        trilha.setDescricao(trilhaAtualizada.getDescricao());
        trilha.setImagem(trilhaAtualizada.getImagem());

        return trilhaRepository.save(trilha);
    }

    public void deletar(Long id) {

        TrilhaEntity trilha = detalhar(id);

        trilhaRepository.delete(trilha);
    }

}
