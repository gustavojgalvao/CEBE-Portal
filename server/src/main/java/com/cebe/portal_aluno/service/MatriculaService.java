package com.cebe.portal_aluno.service;

import com.cebe.portal_aluno.dto.request.MatriculaRequestDTO;
import com.cebe.portal_aluno.entity.Aluno;
import com.cebe.portal_aluno.entity.Matricula;
import com.cebe.portal_aluno.entity.Turma;
import com.cebe.portal_aluno.entity.enums.StatusPagamento;
import com.cebe.portal_aluno.exception.RegraDeNegocioException;
import com.cebe.portal_aluno.exception.RecursoNaoEncontradoException;
import com.cebe.portal_aluno.repository.AlunoRepository;
import com.cebe.portal_aluno.repository.MatriculaRepository;
import com.cebe.portal_aluno.repository.TurmaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;
    private final NotificacaoService notificacaoService;

    public MatriculaService(MatriculaRepository matriculaRepository,
                            AlunoRepository alunoRepository,
                            TurmaRepository turmaRepository,
                            NotificacaoService notificacaoService) {
        this.matriculaRepository = matriculaRepository;
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
        this.notificacaoService = notificacaoService;
    }

    public List<Matricula> listarTodos() {
        return matriculaRepository.findAll();
    }

    public Optional<Matricula> buscarPorId(Integer id) {
        return matriculaRepository.findById(id);
    }

    public Matricula salvar(Matricula matricula) {
        return matriculaRepository.save(matricula);
    }

    // Cria a matrícula a partir do DTO { idAluno, idTurma }
    public Matricula criarMatricula(MatriculaRequestDTO dto) {
        Aluno aluno = alunoRepository.findById(dto.idAluno())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado: " + dto.idAluno()));

        Turma turma = turmaRepository.findById(dto.idTurma())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Turma não encontrada: " + dto.idTurma()));

        // Valida se há vagas disponíveis
        int vagasDisponiveis = turma.getLotacaoMaxima() - turma.getVagasOcupadas();
        if (vagasDisponiveis <= 0) {
            throw new RegraDeNegocioException("Turma sem vagas disponíveis: " + turma.getId());
        }

        // Valida se o aluno já está matriculado nessa turma
        boolean jaMatriculado = matriculaRepository.findByAlunoId(aluno.getId())
                .stream()
                .anyMatch(m -> m.getTurma().getId().equals(turma.getId()));
        if (jaMatriculado) {
            throw new RegraDeNegocioException("Aluno já matriculado nesta turma: " + turma.getId());
        }

        // Decrementa vagas ocupadas na turma
        turma.setVagasOcupadas(turma.getVagasOcupadas() + 1);
        turmaRepository.save(turma);

        Matricula matricula = new Matricula();
        matricula.setAluno(aluno);
        matricula.setTurma(turma);
        matricula.setDataInscricao(LocalDate.now());
        matricula.setStatusPagamento(StatusPagamento.Pendente);

        Matricula salva = matriculaRepository.save(matricula);

        // Cria notificação automática de matrícula
        String cursoNome = turma.getCursos() != null ? turma.getCursos().getNome() : "curso #" + turma.getId();
        notificacaoService.criar(aluno,
                "Matrícula confirmada em " + cursoNome + ". Status de pagamento: Pendente.",
                "MATRICULA");

        return salva;
    }

    public List<Matricula> buscarPorAluno(Aluno aluno) {
        return matriculaRepository.findByAlunoId(aluno.getId());
    }

    public void deletar(Integer id) {
        matriculaRepository.deleteById(id);
    }
}