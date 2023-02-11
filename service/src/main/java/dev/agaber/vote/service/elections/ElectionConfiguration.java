package dev.agaber.vote.service.elections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/** Provides dependency injection stuff. */
@Configuration
public class ElectionConfiguration {

  @Bean
  @ElectionService.ElectionStore
  Map<String, Election> provideElectionStore() {
    return new HashMap<>();
  }
}
