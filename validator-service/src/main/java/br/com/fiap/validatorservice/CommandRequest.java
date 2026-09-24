package br.com.fiap.validatorservice;

import jakarta.validation.constraints.NotBlank;

public record CommandRequest(@NotBlank String command) {
}
