package br.com.fiap.miningservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MiningCommandListener {

    public static final String QUEUE_NAME = "mining-queue";

    private final CommandCountRepository commandCountRepository;

    @RabbitListener(queues = QUEUE_NAME)
    public void consumeCommand(String command) {
        // "Executa" o comando no robo.
        log.info("Robo executando comando: {}", command);

        var commandCount = commandCountRepository.findById(command)
                .orElseGet(() -> new CommandCount(command, 0L));
        commandCount.setTotal(commandCount.getTotal() + 1);
        commandCountRepository.save(commandCount);
    }

}
