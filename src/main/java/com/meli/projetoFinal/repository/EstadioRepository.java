package com.meli.projetoFinal.repository;

import com.meli.projetoFinal.model.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadioRepository extends JpaRepository<Estadio, Long> {

    boolean existsEstadioByNome(String nome);
}
