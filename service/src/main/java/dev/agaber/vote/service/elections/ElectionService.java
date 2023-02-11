package dev.agaber.vote.service.elections;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.UUID.randomUUID;

import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

/**
 * Performs all business logic related to elections. For early iterations, this class will perform
 * all logic in memory.
 */
@Service
final class ElectionService {
  /** Injection annotation for the election store Map. */
  @java.lang.annotation.Documented
  @java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
  @javax.inject.Qualifier
  public @interface ElectionStore {
  }

  private final Map<String, Election> electionStore;

  @Inject
  ElectionService(@ElectionStore Map<String, Election> electionStore) {
    this.electionStore = electionStore;
  }

  public Election createElection(Election election) {
    checkArgument(
        election.id() == null,
        "Cannot create an election if an ID has already been set");
    var newId = randomUUID().toString();
    var created = Election.builder()
        .id(newId)
        .question(election.question())
        .options(election.options())
        .build();
    electionStore.put(newId, created);
    return created;
  }

  public ImmutableList<Election> listElections() {
    return ImmutableList.copyOf(electionStore.values());
  }
}
