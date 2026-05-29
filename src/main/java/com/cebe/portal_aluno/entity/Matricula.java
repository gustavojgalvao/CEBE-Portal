package com.cebe.portal_aluno.entity;

import com.cebe.portal_aluno.entity.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "matricula")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_ALUNO", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "ID_TURMA", nullable = false)
    private Turma turma;

    @Column(name = "DATA_INSCRICAO", nullable = false)
    private LocalDate dataInscricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_PAGAMENTO", nullable = false)
    private StatusPagamento statusPagamento;
}