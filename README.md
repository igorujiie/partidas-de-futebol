# Partidas de Futebol ⚽

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Maven](https://img.shields.io/badge/Maven-Build-orange)
![Testes](https://img.shields.io/badge/Testes-JUnit%205%20%7C%20Mockito-yellow)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

Aplicação Spring Boot para gerenciamento de partidas de futebol, clubes, estádios e rankings. Permite cadastrar, atualizar, consultar e excluir partidas, além de fornecer estatísticas como retrospecto de clubes, confrontos diretos e ranking por critérios.

## Funcionalidades

- Cadastro, atualização e exclusão de partidas
- Consulta de partidas paginada
- Validação de regras de negócio (clubes ativos, datas, conflitos de agenda)
- Retrospecto de clubes (vitórias, empates, derrotas, gols)
- Retrospecto contra adversários
- Confronto direto entre clubes
- Ranking de clubes por pontos, gols, vitórias ou jogos

## Tecnologias

- Java 17+
- Spring Boot
- Maven
- JUnit 5 e Mockito para testes

## Como rodar

1. Clone o repositório:
   ```sh
   git clone https://github.
   


## Collection de Testes (Postman)

Você pode testar a API utilizando a collection do Postman disponível no repositório.

1. Baixe a collection: **partidas-de-futebol.postman_collection.json**
2. Importe no Postman:  
   - Abra o Postman  
   - Clique em "Import"  
   - Selecione o arquivo baixado

A collection contém exemplos de requisições para todos os endpoints principais da aplicação.

## Swagger

A aplicação possui uma documentação Swagger, que pode ser acessada em: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

1. Inicie a aplicação
2. Acesse o link acima no seu navegador
3. Explore os endpoints e seus detalhes


