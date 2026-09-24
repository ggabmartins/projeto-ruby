# Space Mining

Sistema de coordenacao de comandos para robos mineradores, com 4 servicos Spring Boot
independentes (Eureka + RestTemplate + RabbitMQ + retry nativo com backoff exponencial).

## Servicos

| Servico            | Porta | Funcao                                                              |
|---------------------|-------|----------------------------------------------------------------------|
| eureka-server        | 8761  | Service discovery                                                    |
| command-service       | 8080  | Recebe `POST /command` e encaminha para o validator-service          |
| validator-service      | 8082  | Valida o comando, simula falha (50%) com retry/backoff, publica no Rabbit |
| mining-service        | 8081  | Consome a fila, executa o comando (log) e grava contagem no H2       |

## Como rodar (IntelliJ)

1. Suba o RabbitMQ (uma unica vez, a partir desta pasta):

   ```bash
   docker compose up -d
   ```

   Management UI: http://localhost:15672 (usuario `myuser`, senha `secret`)

2. Abra o IntelliJ nesta pasta (`space-mining/space-mining`) e anexe cada subpasta como um
   Gradle project (`View > Tool Windows > Gradle` > `+` > selecione o `build.gradle` de cada
   servico), ou abra 4 janelas separadas, uma por servico.

3. Rode os `*Application` **nesta ordem**:
   1. `EurekaServerApplication` (aguarde subir - http://localhost:8761)
   2. `ValidatorServiceApplication`
   3. `CommandServiceApplication`
   4. `MiningServiceApplication`

## Como testar

```bash
curl -X POST http://localhost:8080/command \
  -H "Content-Type: application/json" \
  -d '{"command":"LEFT"}'
```

- No log do `validator-service`, voce vera a simulacao de falha (~50% das vezes) e as
  tentativas com backoff exponencial ate a mensagem ser publicada com sucesso.
- No log do `mining-service`, voce vera o comando sendo "executado" pelo robo.
- Para ver as contagens acumuladas:

  ```bash
  curl http://localhost:8081/commands
  ```

  Exemplo de resposta:

  ```json
  { "LEFT": 10, "FRONT": 40, "OPEN": 8 }
  ```

- Comandos validos: `RIGHT, LEFT, FRONT, BACK, OPEN, CLOSE`. Qualquer outro valor retorna
  `400 Bad Request` do `validator-service`.
# projeto-ruby
