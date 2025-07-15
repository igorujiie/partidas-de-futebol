package com.meli.projetoFinal.repository;

import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.model.Estado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClubeRepository extends JpaRepository<Clube, Long> {

    boolean existsByNomeAndEstado(String nome, Estado estado);
    Page<Clube> findByNome(String nome, Pageable pageable);
    Page<Clube> findByEstado(Estado estado, Pageable pageable);
    Page<Clube> findByAtivo(Boolean ativo, Pageable pageable);
    Page<Clube> findByNomeAndEstadoAndAtivo(String nome, Estado estado,Boolean ativo, Pageable pageable);
}