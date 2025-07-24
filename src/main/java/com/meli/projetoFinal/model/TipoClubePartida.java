package com.meli.projetoFinal.model;

public enum TipoClubePartida {

    CASA("Casa", 0),
    VISITANTE("Visitante", 1);

    private final String descricao;
    private final int codigo;

    TipoClubePartida(String descricao, int codigo) {
        this.descricao = descricao;
        this.codigo = codigo;
    }

    public static TipoClubePartida getByCodigo(int codigo) {
        for (TipoClubePartida tipo : values()) {
            if (tipo.getCodigo() == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }

    public String getDescricao() {
        return descricao;
    }

    public int getCodigo() {
        return codigo;
    }
}
