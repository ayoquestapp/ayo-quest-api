package br.com.ayo_quest.ayo_quest.service;
import br.com.ayo_quest.ayo_quest.dto.ModuloCadastroDTO;
import br.com.ayo_quest.ayo_quest.dto.ModuloDTO;
import br.com.ayo_quest.ayo_quest.dto.TrilhaResumoDTO;
import br.com.ayo_quest.ayo_quest.models.*;
import br.com.ayo_quest.ayo_quest.repository.ModuloRepository;
import br.com.ayo_quest.ayo_quest.repository.TrilhaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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


        if(dto.getTrilha() != null){

            TrilhaEntity trilha =
                    trilhaRepository.findById(dto.getTrilha().getId())
                            .orElseThrow(() ->
                                    new RuntimeException("Trilha não encontrada")
                            );

            modulo.setTrilha(trilha);
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

            return new ModuloDTO(
                    modulo.getId(),
                    modulo.getNome(),
                    modulo.getDescricao(),
                    modulo.getCargaHoraria(),
                    modulo.getXpAoConcluir(),
                    trilhaDTO
            );

        }).toList();
    }

    @Transactional
    public void deletarModulo(Long id) {
        ModuloEntity modulo = repository.findById(id).orElseThrow();

        for (QuestaoEntity questao : modulo.getQuestoes()) {
            for (AlternativaEntity alt : questao.getAlternativas()) {
                alt.setQuestao(null);
            }
            questao.getAlternativas().clear();
        }


        for (QuestaoEntity questao : modulo.getQuestoes()) {
            questao.setModulo(null);
        }
        modulo.getQuestoes().clear();

        for (ConteudoEntity conteudo : modulo.getConteudos()) {
            conteudo.setModulo(null);
        }
        modulo.getConteudos().clear();

        repository.delete(modulo);
    }

    public ModuloEntity atualizar(Long id, ModuloEntity modulo) {
        modulo.setId(id);

        if (modulo.getConteudos() != null) {
            modulo.getConteudos().forEach(c -> c.setModulo(modulo));
        }

        if (modulo.getQuestoes() != null) {
            modulo.getQuestoes().forEach(q -> {
                q.setModulo(modulo);

                if (q.getAlternativas() != null) {
                    q.getAlternativas().forEach(a -> a.setQuestao(q));
                }
            });
        }

        modulo.getQuestoes().forEach(q -> {
            if (q.getTipo() == null) {
                throw new RuntimeException("Tipo da questão é obrigatório");
            }
        });

        return repository.save(modulo);
    }

    public ModuloEntity buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo não encontrado"));
    }

    public List<ModuloDTO> buscarPorTrilha(Long id){

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
                        )
                ))
                .toList();

    }
}
