package com.cebe.portal_aluno.dto.request;

public class TurmaRequestDTO {

    private Integer idCursos;
    private Integer idProfessor;
    private String turno;
    private Integer lotacaoMaxima;
    private Integer vagasOcupadas;

    public Integer getIdCursos() {
        return idCursos;
    }

    public void setIdCursos(Integer idCursos) {
        this.idCursos = idCursos;
    }

    public Integer getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(Integer idProfessor) {
        this.idProfessor = idProfessor;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public Integer getLotacaoMaxima() {
        return lotacaoMaxima;
    }

    public void setLotacaoMaxima(Integer lotacaoMaxima) {
        this.lotacaoMaxima = lotacaoMaxima;
    }

    public Integer getVagasOcupadas() {
        return vagasOcupadas;
    }

    public void setVagasOcupadas(Integer vagasOcupadas) {
        this.vagasOcupadas = vagasOcupadas;
    }
}
