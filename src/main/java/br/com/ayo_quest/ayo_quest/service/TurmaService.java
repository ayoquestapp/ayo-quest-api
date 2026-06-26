package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.dto.TurmaDTO;

import br.com.ayo_quest.ayo_quest.models.TrilhaEntity;
import br.com.ayo_quest.ayo_quest.models.TurmaEntity;
import br.com.ayo_quest.ayo_quest.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurmaService {
    @Autowired
    private TurmaRepository turmaRepository;

    public List<TurmaDTO> listar() {
        List<TurmaEntity> turmas = turmaRepository.findAll();

      return turmas.stream().map(turma -> new TurmaDTO(
              turma.getCodTurma(),
              turma.getTxNomeTurma(),
              turma.getQuantidadeAlunos(),
              turma.getPeriodo(),
              turma.getResponsavel(),
              turma.getDescricao(),
              turma.getCreatedAt(),
              turma.getCreatedBy(),
              turma.getUpdatedAt(),
              turma.getStTurma()
      )).toList();

    }

    public TurmaEntity detalhar(Long id) {

        return turmaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Turma não encontrada"));
    }

    public TurmaEntity criar(TurmaEntity turma) {

        return turmaRepository.save(turma);
    }

    public TurmaEntity atualizar(Long id, TurmaEntity turmaAtualizada) {

        TurmaEntity turma = detalhar(id);
        turma.setCodTurma(turmaAtualizada.getCodTurma());
        turma.setTxNomeTurma(turmaAtualizada.getTxNomeTurma());
        turma.setDescricao(turmaAtualizada.getDescricao());
        turma.setPeriodo(turmaAtualizada.getPeriodo());
        turma.setResponsavel(turmaAtualizada.getResponsavel());
        turma.setStTurma(turmaAtualizada.getStTurma());

        return turmaRepository.save(turma);
    }

    public void deletar(Long id) {

        TurmaEntity turma = detalhar(id);

        turmaRepository.delete(turma);
    }
}
