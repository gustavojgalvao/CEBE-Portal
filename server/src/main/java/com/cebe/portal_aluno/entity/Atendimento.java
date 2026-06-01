package com.cebe.portal_aluno.entity;

import com.cebe.portal_aluno.entity.enums.StatusAtendimento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "atendimento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_ALUNO", nullable = false)
    private Aluno aluno;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_ATENDIMENTO", nullable = false)
    private StatusAtendimento statusAtendimento;

    @Column(name = "MENSAGEM", nullable = false, length = 500)
    private String mensagem;

    @Column(name = "DATA_HORA", nullable = false)
    private LocalDateTime dataHora;
}
