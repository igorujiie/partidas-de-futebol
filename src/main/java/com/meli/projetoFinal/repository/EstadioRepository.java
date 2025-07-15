package com.meli.projetoFinal.repository;

import com.meli.projetoFinal.model.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadioRepository extends JpaRepository<Estadio, Long> {

    boolean existsEstadioByNome(String nome);
}
