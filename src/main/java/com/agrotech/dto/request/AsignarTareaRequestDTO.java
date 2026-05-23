package com.agrotech.dto.request;

public class AsignarTareaRequestDTO {

    private Integer idOperario;
    private Integer idAuxiliar;

    public AsignarTareaRequestDTO() {}

    public Integer getIdOperario() { return idOperario; }
    public void setIdOperario(Integer idOperario) { this.idOperario = idOperario; }

    public Integer getIdAuxiliar() { return idAuxiliar; }
    public void setIdAuxiliar(Integer idAuxiliar) { this.idAuxiliar = idAuxiliar; }
}