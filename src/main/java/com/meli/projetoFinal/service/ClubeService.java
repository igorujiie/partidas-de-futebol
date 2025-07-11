package com.meli.projetoFinal.service;

        import com.meli.projetoFinal.Exception.ConflitoDeDadosException;
        import com.meli.projetoFinal.Exception.DadoNaoEncontradoException;
        import com.meli.projetoFinal.dto.ClubeRequestDTO;
        import com.meli.projetoFinal.model.Clube;
        import com.meli.projetoFinal.model.Estado;
        import com.meli.projetoFinal.repository.ClubeRepository;

        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.Pageable;
        import org.springframework.stereotype.Service;

        import java.time.LocalDate;

        @Service
        public class ClubeService {

            @Autowired
            private ClubeRepository clubeRepository;


            public Clube cadastrarClube(ClubeRequestDTO dto) {
                if (clubeRepository.existsByNomeAndEstado(dto.getNome(), dto.getEstado())) {
                    throw new ConflitoDeDadosException("Já existe um clube com esse nome nesse estado.");
                }
                Clube clube = new Clube();
                clube.setNome(dto.getNome().toUpperCase());
                clube.setEstado(dto.getEstado());
                clube.setDataCriacao(dto.getDataCriacao());
                clube.setAtivo(dto.getAtivo());
                return clubeRepository.save(clube);
            }

            public Clube atualizarClube(Long id, ClubeRequestDTO clubeRequestDTO) {

                if(!clubeRepository.existsById(id)) {
                    throw new DadoNaoEncontradoException("Clube não encontrado");
                }
                if (clubeRequestDTO.getDataCriacao() == null || clubeRequestDTO.getDataCriacao().isAfter(LocalDate.now())) {
                    throw new ConflitoDeDadosException("Data de criação inválida ou no futuro");
                }

                Clube clube = clubeRepository.findById(id).orElseThrow();
                clube.setNome(clubeRequestDTO.getNome());
                clube.setEstado(clubeRequestDTO.getEstado());
                clube.setDataCriacao(clubeRequestDTO.getDataCriacao());
                clube.setAtivo(clubeRequestDTO.getAtivo());
                return clubeRepository.save(clube);
            }

            public Clube buscarClubePorId(Long id) {
                return clubeRepository.findById(id)
                        .orElseThrow(() -> new DadoNaoEncontradoException("Clube não encontrado"));
            }

            public Page<Clube> buscarTodosClubes(String nome, Estado estado, Boolean ativo, Pageable pageable) {
                if (nome != null && estado != null && ativo != null) {
                    return clubeRepository.findByNomeAndEstadoAndAtivo(nome, estado, ativo, pageable);
                } else if (nome != null) {
                    return clubeRepository.findByNome(nome, pageable);
                } else if (estado != null) {
                    return clubeRepository.findByEstado(estado, pageable);
                } else if (ativo != null) {
                    return clubeRepository.findByAtivo(ativo, pageable);
                } else {
                    return clubeRepository.findAll(pageable);
                }
            }

            public void deletarClube(Long id) {

                if (!clubeRepository.existsById(id)) {
                    throw new DadoNaoEncontradoException("Clube não encontrado");
                }
                Clube clube = clubeRepository.findById(id).orElseThrow();
                clube.setAtivo(false);
                clubeRepository.save(clube);
            }
        }