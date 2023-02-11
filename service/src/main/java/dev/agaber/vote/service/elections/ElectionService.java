package dev.agaber.vote.service.elections;

import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Service;

/**
 * Performs all business logic related to elections. For early iterations, this class will perform
 * all logic in memory.
 */
@Service
final class ElectionService {
  public ImmutableList<Election> listElections() {
    var e1 =
        Election.builder()
            .question("What do?")
            .options(ImmutableList.of("nothing", "sup?", "what do?", "?"))
            .build();
    var e2 = Election.builder()
        .question("Cheeseburger?")
        .options(ImmutableList.of("yes please", "yes!", "ok"))
        .build();
    return ImmutableList.of(e1, e2);
  }
}
