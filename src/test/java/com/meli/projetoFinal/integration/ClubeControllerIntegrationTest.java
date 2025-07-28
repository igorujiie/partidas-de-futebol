package com.meli.projetoFinal.integration;

import com.meli.projetoFinal.dto.request.ClubeDTO;
import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.model.Estado;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClubeControllerIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void deveCadastrarClubeComSucesso() {
        ClubeDTO clubeDTO = new ClubeDTO();
        clubeDTO.setNome("Clube Exemplo");
        clubeDTO.setEstado(Estado.SP.name());
        clubeDTO.setAtivo(true);
        clubeDTO.setDataCriacao(LocalDate.now());

        ResponseEntity<Clube> response = restTemplate.postForEntity("http://localhost:" + port + "/clube", clubeDTO, Clube.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNome()).isEqualTo("CLUBE EXEMPLO");
    }

    @Test
    void deveBuscarTodosClubes() {
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/clube", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void deveBuscarClubePorIdComSucesso() {
        ClubeDTO clubeDTO = new ClubeDTO();
        clubeDTO.setNome("Clube Teste");
        clubeDTO.setEstado(Estado.SP.name());
        clubeDTO.setAtivo(true);
        clubeDTO.setDataCriacao(LocalDate.now());

        ResponseEntity<Clube> postResponse = restTemplate.postForEntity("http://localhost:" + port + "/clube", clubeDTO, Clube.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postResponse.getBody()).isNotNull();

        Long id = postResponse.getBody().getId();

        ResponseEntity<Clube> getResponse = restTemplate.getForEntity("http://localhost:" + port + "/clube/" + id, Clube.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getId()).isEqualTo(id);
        assertThat(getResponse.getBody().getNome()).isEqualTo("CLUBE TESTE");
    }

    @Test
    void deveAtualizarClubeComSucesso() {
        ClubeDTO clubeDTO = new ClubeDTO();
        clubeDTO.setNome("Clube Original");
        clubeDTO.setEstado(Estado.SP.name());
        clubeDTO.setAtivo(true);
        clubeDTO.setDataCriacao(LocalDate.now());

        ResponseEntity<Clube> postResponse = restTemplate.postForEntity("http://localhost:" + port + "/clube", clubeDTO, Clube.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postResponse.getBody()).isNotNull();

        Long id = postResponse.getBody().getId();

        ClubeDTO clubeAtualizadoDTO = new ClubeDTO();
        clubeAtualizadoDTO.setNome("Clube Atualizado");
        clubeAtualizadoDTO.setEstado(Estado.RJ.name());
        clubeAtualizadoDTO.setAtivo(false);
        clubeAtualizadoDTO.setDataCriacao(LocalDate.now());

        HttpEntity<ClubeDTO> requestEntity = new HttpEntity<>(clubeAtualizadoDTO);
        ResponseEntity<Clube> putResponse = restTemplate.exchange("http://localhost:" + port + "/clube/" + id, HttpMethod.PUT, requestEntity, Clube.class);

        assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(putResponse.getBody()).isNotNull();
        assertThat(putResponse.getBody().getNome()).isEqualTo("CLUBE ATUALIZADO");
        assertThat(putResponse.getBody().getEstado()).isEqualTo(Estado.RJ);
        assertThat(putResponse.getBody().getAtivo()).isFalse();
    }
}

