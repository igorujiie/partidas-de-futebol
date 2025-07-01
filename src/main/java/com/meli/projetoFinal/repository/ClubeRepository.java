package com.meli.projetoFinal.repository;

import com.meli.projetoFinal.dto.ClubeRequestDTO;
import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubeRepository extends JpaRepository<Clube, Long> {
    boolean existsByNomeAndEstado(String nome, Estado estado);
}
