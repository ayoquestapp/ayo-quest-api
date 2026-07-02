package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.dto.PeriodoDTO;
import br.com.ayo_quest.ayo_quest.dto.ResponsavelDTO;
import br.com.ayo_quest.ayo_quest.dto.TurmaCadastroDTO;
import br.com.ayo_quest.ayo_quest.dto.TurmaDTO;

import br.com.ayo_quest.ayo_quest.enuns.TiposPeriodos;
import br.com.ayo_quest.ayo_quest.models.ProfileEntity;
import br.com.ayo_quest.ayo_quest.models.TrilhaEntity;
import br.com.ayo_quest.ayo_quest.models.TurmaEntity;
import br.com.ayo_quest.ayo_quest.repository.ProfileImpl;
import br.com.ayo_quest.ayo_quest.repository.ProfileRepository;
import br.com.ayo_quest.ayo_quest.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class TurmaService {
    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileImpl profileImpl;

    public List<TurmaDTO> listar() {
        List<TurmaEntity> turmas = turmaRepository.findAll();


        return turmas.stream().map(turma -> {
            ResponsavelDTO responsavel = profileImpl.detalharResposanvel(turma.getResponsavel());



                return new TurmaDTO(
                turma.getId(),
                turma.getCodTurma(),
                turma.getTxNomeTurma(),
                turma.getQuantidadeAlunos(),
                turma.getPeriodo(),
                responsavel,
                turma.getDescricao(),
                turma.getCreatedAt(),
                turma.getCreatedBy(),
                turma.getUpdatedAt(),
                turma.getStTurma()
                );
        }).toList();

    }

    public TurmaEntity detalhar(Long id) {

        return turmaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Turma não encontrada"));
    }

    public TurmaDTO criar(TurmaCadastroDTO dto) {

        TurmaEntity turmaEntity = new TurmaEntity();

        turmaEntity.setTxNomeTurma(dto.getTxNomeTurma());
        turmaEntity.setCodTurma(dto.getCodTurma());
        turmaEntity.setDescricao(dto.getDescricao());
        turmaEntity.setResponsavel(dto.getResponsavel());
        turmaEntity.setPeriodo(dto.getPeriodo());

        turmaEntity.setQuantidadeAlunos(
                dto.getAlunos() == null ? 0L : (long) dto.getAlunos().size()
        );

        TurmaEntity turmaSalva = turmaRepository.save(turmaEntity);
        ResponsavelDTO responsavelDTO = profileImpl.detalharResposanvel(turmaSalva.getResponsavel());
        return new TurmaDTO(
                turmaSalva.getId(),
                turmaSalva.getCodTurma(),
                turmaSalva.getTxNomeTurma(),
                turmaSalva.getQuantidadeAlunos(),
                turmaSalva.getPeriodo(),
                responsavelDTO,
                turmaSalva.getDescricao(),
                turmaSalva.getCreatedAt(),
                turmaSalva.getCreatedBy(),
                turmaSalva.getUpdatedAt(),
                turmaSalva.getStTurma()
        );
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

    public List<String> listarPeriodos() {
        return Arrays.stream(TiposPeriodos.values())
                .map(Enum::name)
                .toList();
    }
}
