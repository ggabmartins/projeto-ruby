package br.com.fiap.validatorservice;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ValidatorController {

    private final ValidatorPublisher validatorPublisher;

    @PostMapping("/validate")
    public ResponseEntity<String> validate(@Valid @RequestBody CommandRequest request) {
        String command = request.command().toUpperCase();

        boolean valid = java.util.Arrays.stream(MiningCommand.values())
                .anyMatch(c -> c.name().equals(command));

        if (!valid) {
            return ResponseEntity.badRequest().body("Comando inválido: " + request.command());
        }

        validatorPublisher.publish(command);
        return ResponseEntity.ok("Comando validado e publicado: " + command);
    }

    // Se todas as tentativas de retry se esgotarem, retorna 503 em vez de derrubar a requisição.
    @ExceptionHandler(SimulatedCommunicationFailureException.class)
    public ResponseEntity<String> handleCommunicationFailure(SimulatedCommunicationFailureException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());
    }

}
