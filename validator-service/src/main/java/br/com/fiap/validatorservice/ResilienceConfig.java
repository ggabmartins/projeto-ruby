package br.com.fiap.validatorservice;

import org.springframework.context.annotation.Configuration;
import org.springframework.resilience.annotation.EnableResilientMethods;

// Habilita o processamento da anotação @Retryable (suporte nativo do Spring Framework 7 / Boot 4).
@Configuration
@EnableResilientMethods
public class ResilienceConfig {
}
