# Frame Extrator API

Projeto backend do Hackaton da FIAP - Fase 5

## Tecnologias utilizadas

- Java
- Spring Boot
- Maven
- JUnit
- Mockito

## Extrutura do projeto

- `src/main/java`: Contem o código da aplicação
- `src/test/java`: Contem o código de testes da aplicação
- `src/test/resources`: Contem os arquivos de configuração

## Rodando a aplicação

Para rodar a aplicação, use o seguinte comando:

```sh
mvn spring-boot:run
```
Também é possível rodar toda a aplicação usando banco de dados postgres configurado nas variaveis de ambiente:

```sh
docker-compose up -d
```