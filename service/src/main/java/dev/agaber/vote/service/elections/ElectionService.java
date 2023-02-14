package dev.agaber.vote.service.elections;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.UUID.randomUUID;
import static java.util.function.Predicate.not;

import dev.agaber.vote.service.elections.inject.Annotations.ElectionStore;
import dev.agaber.vote.service.elections.inject.Annotations.VoteStore;
import dev.agaber.vote.service.elections.model.Election;
import dev.agaber.vote.service.elections.model.Vote;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Performs all business logic related to elections. For early iterations, this class will perform
 * all logic in memory.
 */
@Service
final class ElectionService {
  private final Map<String, Election> electionStore;
  private final Multimap<String, Vote> voteStore;

  @Inject
  ElectionService(
      @ElectionStore Map<String, Election> electionStore,
      @VoteStore Multimap<String, Vote> voteStore) {
    this.electionStore = electionStore;
    this.voteStore = voteStore;
  }

  public Election createElection(Election election) {
    checkArgument(
        election.getId() == null,
        "Cannot create an election if an ID has already been set");
    var newId = randomUUID().toString();
    var created = Election.builder()
        .id(newId)
        .question(election.getQuestion())
        .options(election.getOptions())
        .build();
    electionStore.put(newId, created);
    return created;
  }

  public Optional<Election> getById(String id) {
    return Optional.ofNullable(electionStore.getOrDefault(id, null));
  }

  public ImmutableList<Election> listElections() {
    return ImmutableList.copyOf(electionStore.values());
  }

  public void vote(String electionId, ImmutableList<String> choices) {
    checkArgument(electionStore.containsKey(electionId), "No election with ID %s", electionId);
    var election = electionStore.get(electionId);
    checkArgument(
        hasValidChoices(election, choices),
        "Choices did not match election options. Valid options are %s",
        election.getOptions());
    voteStore.put(electionId, Vote.builder().electionId(electionId).choices(choices).build());
  }

  public String tally(String electionId) {
    checkArgument(electionStore.containsKey(electionId), "No election with ID %s", electionId);
    var votes = ImmutableList.copyOf(voteStore.get(electionId));
    return votes.isEmpty() ? "" : tally(votes, new HashSet<>());
  }

  private static String tally(ImmutableList<Vote> votes, Set<String> eliminated) {
    var counter = new HashMap<String, Integer>();
    var numVoters = votes.size();

    for (var vote : votes) {
      // Count the first choice that isn't eliminated yet.
      var maybeChoice = vote.getChoices().stream().filter(not(eliminated::contains)).findFirst();
      if (maybeChoice.isEmpty()) {
        continue;
      }

      var choice = maybeChoice.get();
      counter.put(choice, counter.getOrDefault(choice, 0) + 1);

      // Stop once we find any selection that has majority of votes.
      int choiceCount = counter.get(choice);
      if (choiceCount / (numVoters * 1.0) * 100 >= 50.0) {
        return choice;
      }
    }

    eliminated.add(getLowestVoteGetter(counter));
    return tally(votes, eliminated);
  }

  /** Finds the choice with the lowest votes. */
  private static String getLowestVoteGetter(Map<String, Integer> counter) {
    var minChoice = "";
    var minCount = Integer.MAX_VALUE;
    for (var choice : counter.keySet()) {
      int count = counter.get(choice);
      if (count < minCount) {
        minChoice = choice;
        minCount = count;
      }
    }
    return minChoice;
  }

  private static boolean hasValidChoices(Election election, ImmutableList<String> choices) {
    return ImmutableSet.copyOf(election.getOptions()).containsAll(choices);
  }
}