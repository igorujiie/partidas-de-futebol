package com.meli.projetoFinal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="partidas")
public class Partidas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "clube_casa_id")
    private Clube clubeCasa;


    @ManyToOne(optional = false)
    @JoinColumn(name = "clube_visitante_id")
    private Clube clubeVisitante;


    @ManyToOne(optional = false)
    @JoinColumn(name = "clube_estadio_id")
    private Estadio estadio;

    @NotNull(message = "Gols casa são obrigatórios")
    @Min(value = 0, message = "Gols não pode ser negativo")
    private Integer golsCasa;

    @NotNull(message = "Gols visitante são obrigatórios")
    @Min(value = 0, message = "Gols não pode ser negativo")
    private Integer golsVisitante;

    @Column(name = "data_hora", nullable = false)
    private LocalDate dataPartida;


}
