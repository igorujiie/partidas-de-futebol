package com.meli.projetoFinal.templates;

import com.meli.projetoFinal.dto.EstadioDTO;

public class EstadioTemplate {

    public static EstadioDTO payloadDeSucesso(){
        EstadioDTO estadioDTO = new EstadioDTO();
        estadioDTO.setNome("Estadio 1");
        return estadioDTO;
    }

    public static EstadioDTO payloadDeDadosInvalidos(){
        EstadioDTO estadioDTO = new EstadioDTO();
        estadioDTO.setNome(null);
        return estadioDTO;
    }
}
