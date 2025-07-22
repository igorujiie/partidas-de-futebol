package com.meli.projetoFinal.repository;

import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.model.Estadio;
import com.meli.projetoFinal.model.Partidas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PartidasRepository extends JpaRepository<Partidas, Long> {
    boolean existsByClubeCasaAndDataPartidaBetween(Clube clubeCasa, LocalDate min, LocalDate max);
    boolean existsByClubeVisitanteAndDataPartidaBetween(Clube clubeVisitante, LocalDate min, LocalDate max);
    boolean existsByEstadioAndDataPartida(Estadio estadio, LocalDate dataPartida);

    @Query(value = "SELECT  * from projetofinal.partidas p where p.gols_casa - p.gols_visitante >= 3 or p.gols_visitante  -p.gols_casa  >=3", nativeQuery = true)
    Page<Partidas> buscarPartidasGoleadas(Pageable pageable, @Param("goleadas") boolean goleadas);
}
