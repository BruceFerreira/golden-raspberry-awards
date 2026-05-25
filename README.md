# Golden Raspberry Awards API

API REST para consultar os produtores com maior e menor intervalo entre prêmios consecutivos do Golden Raspberry Awards.

## Tecnologias

- Java 21
- Spring Boot 3.5.13
- H2
- SpringDoc OpenAPI (Swagger)
- Lombok

## Como rodar

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`. Os dados do CSV são carregados automaticamente na inicialização.

## Como rodar os testes

```bash
# Windows
.\mvnw.cmd test

# Linux / macOS
./mvnw test
```

## Endpoint

```
GET /v1/producers/awards-interval
```

Retorna os produtores com o menor e o maior intervalo entre dois prêmios consecutivos.

**Exemplo de resposta:**

```json
{
  "min": [
    {
      "producer": "Joel Silver",
      "interval": 1,
      "previousWin": 1990,
      "followingWin": 1991
    }
  ],
  "max": [
    {
      "producer": "Matthew Vaughn",
      "interval": 13,
      "previousWin": 2002,
      "followingWin": 2015
    }
  ]
}
```

## Swagger

Disponível em `http://localhost:8080/swagger-ui.html` após subir a aplicação.

## Arquitetura

O projeto segue arquitetura hexagonal (Ports & Adapters), separando domínio, casos de uso e infraestrutura.

```
src/main/java/com/outsera/goldenraspberry/
├── domain/          # regras de negócio (records Java 21, sem dependências externas)
├── application/     # casos de uso
├── adapter/         # web (controllers) e persistence (JPA + H2)
└── infrastructure/  # configurações, CSV loader, logging
```
