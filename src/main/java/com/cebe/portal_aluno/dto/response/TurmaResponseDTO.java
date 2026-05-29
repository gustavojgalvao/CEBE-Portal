package com.cebe.portal_aluno.dto.response;

public class TurmaResponseDTO {

    private Integer id;
    private String turno;
    private Integer lotacaoMaxima;
    private Integer vagasOcupadas;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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