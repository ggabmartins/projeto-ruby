package br.com.fiap.validatorservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidatorPublisher {

    private final RabbitTemplate rabbitTemplate;

    // Simula falha de comunicação com 50% de chance. Em caso de falha, tenta novamente
    // com backoff exponencial (200ms, 400ms, 800ms, 1600ms...). Em caso de sucesso, publica no RabbitMQ.
    @Retryable(
            includes = SimulatedCommunicationFailureException.class,
            maxRetries = 4,
            delay = 200,
            multiplier = 2,
            jitter = 50,
            maxDelay = 3000
    )
    public void publish(String command) {
        if (Math.random() < 0.5) {
            log.warn("Falha simulada de comunicação ao validar o comando: {}", command);
            throw new SimulatedCommunicationFailureException("Falha simulada ao validar comando " + command);
        }

        log.info("Comando validado com sucesso: {}. Publicando na fila...", command);
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, command);
    }

}
