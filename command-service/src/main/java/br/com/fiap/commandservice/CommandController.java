package br.com.fiap.commandservice;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommandController {

    private static final String VALIDATOR_URL = "http://validator-service/validate";

    private final RestTemplate restTemplate;

    @PostMapping("/command")
    public ResponseEntity<String> sendCommand(@Valid @RequestBody CommandRequest request) {
        log.info("Comando recebido: {}", request.command());
        try {
            // Encaminha o comando para o Validator Service, que fica registrado no Eureka.
            ResponseEntity<String> response = restTemplate.postForEntity(VALIDATOR_URL, request, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (RestClientResponseException ex) {
            // RestTemplate lanca excecao para respostas 4xx/5xx: repassa o status e corpo originais do validator-service.
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        }
    }

}
