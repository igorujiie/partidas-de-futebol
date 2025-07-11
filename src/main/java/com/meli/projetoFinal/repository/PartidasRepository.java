package com.meli.projetoFinal.repository;

import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.model.Estadio;
import com.meli.projetoFinal.model.Partidas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface PartidasRepository extends JpaRepository<Partidas, Long> {
    boolean existsByClubeCasaAndDataPartidaBetween(Clube clubeCasa, LocalDate min, LocalDate max);
    boolean existsByClubeVisitanteAndDataPartidaBetween(Clube clubeVisitante, LocalDate min, LocalDate max);
    boolean existsByEstadioAndDataPartida(Estadio estadio, LocalDate dataPartida);
}
