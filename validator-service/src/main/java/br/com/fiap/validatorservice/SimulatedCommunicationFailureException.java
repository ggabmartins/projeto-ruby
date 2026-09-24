package br.com.fiap.validatorservice;

// Representa uma falha de comunicação simulada (50% de chance). É essa exceção que o @Retryable escuta.
public class SimulatedCommunicationFailureException extends RuntimeException {
    public SimulatedCommunicationFailureException(String message) {
        super(message);
    }
}
