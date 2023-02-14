package dev.agaber.vote.service.elections;

import static org.assertj.core.api.Assertions.assertThat;

import dev.agaber.vote.service.elections.model.Election;
import dev.agaber.vote.service.elections.model.Vote;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

final class ElectionServiceTest {
  private ElectionService electionService;
  private Map<String, Election> electionStore;
  private Multimap<String, Vote> voteStore;

  @BeforeEach
  public void setUp() throws Exception {
    electionStore = new HashMap<String, Election>();
    voteStore = ArrayListMultimap.create();
    electionService = new ElectionService(electionStore, voteStore);
  }

  @Test
  public void createElectionShouldSetAnIdAndPushToElectionStore() throws Exception {
    var election = LUNCH_ELECTION.toBuilder().id(null).build();

    // Execute.
    var result = electionService.createElection(election);

    // Verify.
    assertThat(electionStore.size()).isEqualTo(1);
    assertThat(result.getId()).isNotEmpty();
    var expected = election.toBuilder().id(result.getId()).build();
    assertThat(electionStore.get(result.getId())).isEqualTo(expected);
  }

  @Test
  public void listElectionsShouldReturnAllValuesInElectionStore() throws Exception {
    var e1 = LUNCH_ELECTION;
    var e2 = Election.builder()
        .id("2")
        .question("What is the best fruit?")
        .options(ImmutableList.of("apple", "banana", "avocado", "tomato"))
        .build();
    var e3 = Election.builder()
        .id("3")
        .question("What is the best vegetable?")
        .options(ImmutableList.of("carrot", "broccoli", "cauliflower"))
        .build();
    electionStore.put(e1.getId(), e1);
    electionStore.put(e2.getId(), e2);
    electionStore.put(e3.getId(), e3);

    // Execute.
    var result = electionService.listElections();

    // Verify.
    assertThat(result).containsExactly(e1, e2, e3);
  }

  @Test
  public void tally() {
    var electionId = LUNCH_ELECTION.getId();
    electionStore.put(electionId, LUNCH_ELECTION);

    voteStore.put(
        LUNCH_ELECTION.getId(),
        Vote.builder()
            .electionId(electionId)
            .choice("sandwich")
            .build());

    voteStore.put(
        LUNCH_ELECTION.getId(),
        Vote.builder()
            .electionId(electionId)
            .choice("pizza")
            .build());

    voteStore.put(
        LUNCH_ELECTION.getId(),
        Vote.builder()
            .electionId(electionId)
            .choice("pizza")
            .build());

    // Act
    var result = electionService.tally(LUNCH_ELECTION.getId());

    // Assert.
    assertThat(result).isEqualTo("pizza");
  }

  @Test
  public void tally_onevote() {
    var electionId = LUNCH_ELECTION.getId();
    electionStore.put(electionId, LUNCH_ELECTION);

    voteStore.put(
        LUNCH_ELECTION.getId(),
        Vote.builder()
            .electionId(electionId)
            .choice("sandwich")
            .build());

    // Act
    var result = electionService.tally(LUNCH_ELECTION.getId());

    // Assert.
    assertThat(result).isEqualTo("sandwich");
  }

  @Test
  public void tally_tie() {
    var electionId = LUNCH_ELECTION.getId();
    electionStore.put(electionId, LUNCH_ELECTION);

    voteStore.put(
        LUNCH_ELECTION.getId(),
        Vote.builder()
            .electionId(electionId)
            .choice("sandwich")
            .build());

    voteStore.put(
        LUNCH_ELECTION.getId(),
        Vote.builder()
            .electionId(electionId)
            .choice("pizza")
            .build());

    // Act
    var result = electionService.tally(LUNCH_ELECTION.getId());

    // Assert - Expect it to take the first one.
    assertThat(result).isEqualTo("sandwich");
  }

  @Test
  public void tally_novotes() {
    var electionId = LUNCH_ELECTION.getId();
    electionStore.put(electionId, LUNCH_ELECTION);

    // Act
    var result = electionService.tally(LUNCH_ELECTION.getId());

    // Assert - Expect it to take the first one.
    assertThat(result).isEqualTo("");
  }

  private static final Election LUNCH_ELECTION = Election.builder()
      .id("1")
      .question("What do you want for lunch?")
      .options(ImmutableList.of("sandwich", "pizza", "nothing", "fruit"))
      .build();
}
