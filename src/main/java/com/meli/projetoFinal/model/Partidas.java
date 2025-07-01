package com.meli.projetoFinal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "gols_casa", nullable = false)
    private Integer golsMandante;

    @Column(name = "gols_visitante", nullable = false)
    private Integer golsVisitante;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime data;


}
