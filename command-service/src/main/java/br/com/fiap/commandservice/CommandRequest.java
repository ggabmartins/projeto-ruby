package br.com.fiap.commandservice;

import jakarta.validation.constraints.NotBlank;

public record CommandRequest(@NotBlank String command) {
}
