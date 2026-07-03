package br.com.ayo_quest.ayo_quest.service;
import br.com.ayo_quest.ayo_quest.dto.*;
import br.com.ayo_quest.ayo_quest.enuns.StatusUsuarioTurmaEnum;
import br.com.ayo_quest.ayo_quest.enuns.TipoUsuario;
import br.com.ayo_quest.ayo_quest.enuns.TiposPeriodos;
import br.com.ayo_quest.ayo_quest.models.TurmaConviteEntity;
import br.com.ayo_quest.ayo_quest.models.TurmaEntity;
import br.com.ayo_quest.ayo_quest.repository.ProfileImpl;
import br.com.ayo_quest.ayo_quest.repository.ProfileRepository;
import br.com.ayo_quest.ayo_quest.repository.TurmaConviteRepository;
import br.com.ayo_quest.ayo_quest.repository.TurmaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
public class TurmaService {
    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileImpl profileImpl;

    @Autowired
    private TurmaConviteRepository turmaConviteRepository;

    @Autowired
    private EmailService emailService;

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
        criarConvites(turmaSalva, dto.getAlunos());
        System.out.println("ALUNOS: " + dto.getAlunos());
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

    @Transactional
    public void deletar(Long id) {
        turmaConviteRepository.deleteByTurmaId(id);
        turmaRepository.deleteById(id);
    }

    public List<String> listarPeriodos() {
        return Arrays.stream(TiposPeriodos.values())
                .map(Enum::name)
                .toList();
    }

    private void criarConvites(TurmaEntity turma, List<AlunoConviteDTO> alunos) {

        if (alunos == null || alunos.isEmpty()) {
            return;
        }

        ResponsavelDTO responsavel =
                profileImpl.detalharResposanvel(turma.getResponsavel());

        for (AlunoConviteDTO aluno : alunos) {

            if (turmaConviteRepository.existsByTurmaIdAndEmail(
                    turma.getId(),
                    aluno.getEmail())) {
                continue;
            }

            TurmaConviteEntity convite = new TurmaConviteEntity();

            convite.setTurma(turma);
            convite.setEmail(aluno.getEmail());
            convite.setTipo(TipoUsuario.STUDENT);
            convite.setStatus(StatusUsuarioTurmaEnum.PENDENTE);
            convite.setToken(UUID.randomUUID().toString());
            convite.setExpiresAt(LocalDateTime.now().plusDays(7));

            turmaConviteRepository.save(convite);

            String link = "http://localhost:4200/convite/" + convite.getToken();

            emailService.enviarConvite(
                    aluno.getEmail(),
                    responsavel.getNome(),
                    turma.getTxNomeTurma(),
                    link
            );
        }
    }
}
