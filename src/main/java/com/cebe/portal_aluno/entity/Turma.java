package com.cebe.portal_aluno.entity;

import com.cebe.portal_aluno.entity.enums.Turno;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "turma")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_CURSOS", nullable = false)
    private Cursos cursos;

    @ManyToOne
    @JoinColumn(name = "ID_PROFESSOR", nullable = false)
    private Professor professor;

    @Enumerated(EnumType.STRING)
    @Column(name = "TURNO", nullable = false)
    private Turno turno;

    @Column(name = "LOTACAO_MAXIMA", nullable = false)
    private Integer lotacaoMaxima;

    @Column(name = "VAGAS_OCUPADAS", nullable = false)
    private Integer vagasOcupadas;
}
