package dev.agaber.vote.service.elections;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static dev.agaber.vote.service.converters.Converters.toDocument;
import static dev.agaber.vote.service.converters.Converters.toObjectId;
import static dev.agaber.vote.service.converters.Converters.toResource;
import static java.util.function.Predicate.not;

import dev.agaber.vote.service.converters.Converters;
import dev.agaber.vote.service.storage.ElectionDocument;
import dev.agaber.vote.service.storage.ElectionRepository;
import dev.agaber.vote.service.storage.VoteDocument;
import dev.agaber.vote.service.storage.VoteRepository;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** Manages election logic, including storing election configs and tallying votes. */
@Service
final class ElectionService {
  private final ElectionRepository electionRepository;
  private final VoteRepository voteRepository;

  @Inject
  ElectionService(
      ElectionRepository electionRepository,
      VoteRepository voteRepository) {
    this.electionRepository = electionRepository;
    this.voteRepository = voteRepository;
  }

  public Election createElection(Election election) {
    checkArgument(
        election.getId() == null,
        "Cannot create an election if an ID has already been set");
    return toResource(electionRepository.insert(toDocument(election)));
  }

  public Optional<Election> getById(String id) {
    return electionRepository.findById(toObjectId(id)).map(Converters::toResource);
  }

  public ImmutableList<Election> listElections() {
    var documents = electionRepository.findAll();
    return documents.stream().map(Converters::toResource).collect(toImmutableList());
  }

  public Vote vote(String electionId, ImmutableList<String> choices) {
    var maybeElection = electionRepository.findById(toObjectId(electionId));
    checkArgument(maybeElection.isPresent(), "No election with ID %s", electionId);
    checkArgument(
        hasValidChoices(maybeElection.get(), choices),
        "Choices did not match election options. Valid options are %s",
        maybeElection.get().getOptions());
    var electionObjectId = toObjectId(electionId);
    var voteDoc = VoteDocument.builder().electionObjectId(electionObjectId).choices(choices).build();
    return toResource(voteRepository.insert(voteDoc));
  }

  public String tally(String electionId) {
    var maybeElection = electionRepository.findById(new ObjectId(electionId));
    checkArgument(maybeElection.isPresent(), "No election with ID %s", electionId);
    var electionObjectId = toObjectId(electionId);
    var votes = ImmutableList.copyOf(voteRepository.findAllByElectionObjectId(electionObjectId));
    return votes.isEmpty() ? "" : tally(votes, new HashSet<>());
  }

  private static String tally(ImmutableList<VoteDocument> votes, Set<String> eliminated) {
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

  private static boolean hasValidChoices(ElectionDocument election, ImmutableList<String> choices) {
    return ImmutableSet.copyOf(election.getOptions()).containsAll(choices);
  }
}
