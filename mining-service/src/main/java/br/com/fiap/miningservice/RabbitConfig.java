package br.com.fiap.miningservice;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // Declara a fila tambem do lado consumidor (idempotente), para nao depender
    // da ordem de start em relacao ao validator-service, que e quem a declara originalmente.
    @Bean
    public Queue miningQueue() {
        return new Queue(MiningCommandListener.QUEUE_NAME, true);
    }

}
