package dev.agaber.vote.service.elections;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

final class ElectionServiceTest {
  private ElectionService electionService;
  private Map<String, Election> electionStore;

  @BeforeEach
  public void setUp() throws Exception {
    electionStore = new HashMap<String, Election>();
    electionService = new ElectionService(electionStore);
  }

  @Test
  public void createElectionShouldSetAnIdAndPushToElectionStore() throws Exception {
    var election = Election.builder()
        .question("What do you want for lunch?")
        .options(ImmutableList.of("sandwich", "pizza", "nothing", "fruit"))
        .build();

    // Execute.
    var result = electionService.createElection(election);

    // Verify.
    assertThat(electionStore.size()).isEqualTo(1);
    assertThat(result.id()).isNotEmpty();
    var expected = Election.builder()
        .id(result.id())
        .question(election.question())
        .options(election.options())
        .build();
    assertThat(electionStore.get(result.id())).isEqualTo(expected);
  }

  @Test
  public void listElectionsShouldReturnAllValuesInElectionStore() throws Exception {
    var e1 = Election.builder()
        .id("1")
        .question("What do you want for lunch?")
        .options(ImmutableList.of("sandwich", "pizza", "nothing", "fruit"))
        .build();
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
    electionStore.put(e1.id(), e1);
    electionStore.put(e2.id(), e2);
    electionStore.put(e3.id(), e3);

    // Execute.
    var result = electionService.listElections();

    // Verify.
    assertThat(result).containsExactly(e1, e2, e3);
  }
}
