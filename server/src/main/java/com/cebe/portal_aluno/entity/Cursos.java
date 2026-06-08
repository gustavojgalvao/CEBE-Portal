package com.cebe.portal_aluno.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cursos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "CARGA_HORARIA", nullable = false)
    private Integer cargaHoraria;

    @Column(name = "BANNER_URL", length = 255)
    private String bannerUrl;
}
