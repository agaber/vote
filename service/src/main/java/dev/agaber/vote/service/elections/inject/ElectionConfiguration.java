package dev.agaber.vote.service.elections.inject;

import dev.agaber.vote.service.elections.inject.Annotations.ElectionStore;
import dev.agaber.vote.service.elections.inject.Annotations.VoteStore;
import dev.agaber.vote.service.elections.model.Election;
import dev.agaber.vote.service.elections.model.Vote;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/** Provides dependency injection stuff. */
@Configuration
public class ElectionConfiguration {

  @Bean
  @ElectionStore
  Map<String, Election> provideElectionStore() {
    return new HashMap<>();
  }

  @Bean
  @VoteStore
  Multimap<String, Vote> provideVoteStore() {
    return ArrayListMultimap.create();
  }
}
