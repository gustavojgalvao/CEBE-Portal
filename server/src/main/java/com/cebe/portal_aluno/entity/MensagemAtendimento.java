package com.cebe.portal_aluno.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensagem_atendimento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensagemAtendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ATENDIMENTO", nullable = false)
    private Atendimento atendimento;

    // "ALUNO" ou "ADMIN"
    @Column(name = "REMETENTE_TIPO", nullable = false, length = 20)
    private String remetenteTipo;

    @Column(name = "MENSAGEM", nullable = false, length = 1000)
    private String mensagem;

    @Column(name = "DATA_HORA", nullable = false)
    private LocalDateTime dataHora;
}
