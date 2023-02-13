package dev.agaber.vote.service.elections;

import static org.assertj.core.api.Assertions.assertThat;

import dev.agaber.vote.service.elections.inject.Annotations;
import dev.agaber.vote.service.elections.inject.Annotations.ElectionStore;
import dev.agaber.vote.service.elections.inject.Annotations.VoteStore;
import dev.agaber.vote.service.elections.inject.ElectionConfiguration;
import dev.agaber.vote.service.elections.model.Election;
import dev.agaber.vote.service.elections.model.Vote;
import dev.agaber.vote.service.server.VoteServiceApplication;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import javax.inject.Inject;
import java.util.Map;

/** Starts a local server and communicates with actual HTTP REST. */
@ContextConfiguration(classes = {VoteServiceApplication.class, ElectionConfiguration.class})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
final class ElectionsIntegrationTest {
  @Inject
  @ElectionStore
  private Map<String, Election> electionStore;

  @Inject
  @VoteStore
  private Multimap<String, Vote> voteStore;

  @Inject
  private TestRestTemplate restTemplate;

  @Value(value = "${local.server.port}")
  private int port;

  @BeforeEach
  public void setUp() throws Exception {
    // Reset storage to a base state before each test.
    voteStore.clear();
    electionStore.clear();
    electionStore.put(FRUIT_ELECTION.id(), FRUIT_ELECTION);
    electionStore.put(VEGETABLE_ELECTION.id(), VEGETABLE_ELECTION);
  }

  @Test
  public void create() throws Exception {
    var newElection = Election.builder()
        .question("What do you want for lunch?")
        .options(ImmutableList.of("sandwich", "pizza", "nothing", "fruit"))
        .build();

    // Execute.
    var response = restTemplate.postForObject(basePath(), newElection, Election.class);

    // Verify.
    var expected = newElection.toBuilder().id(response.id()).build();
    assertThat(response).isEqualTo(expected);
    assertThat(electionStore.get(response.id())).isEqualTo(expected);
  }

  @Test
  public void create_requestHasId_throwsBadRequestException() throws Exception {
    var newElection = Election.builder()
        .id("123")
        .question("What do you want for lunch?")
        .options(ImmutableList.of("sandwich", "pizza", "nothing", "fruit"))
        .build();
    var response = restTemplate.postForEntity(basePath(), newElection, Election.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  public void getById_found_returnsElection() throws Exception {
    var path = String.format("%s/%s", basePath(), FRUIT_ELECTION.id());
    var response = restTemplate.getForObject(path, Election.class);
    assertThat(response).isEqualTo(FRUIT_ELECTION);
  }

  public void getById_notFound_throwsException() throws Exception {
    var path = String.format("%s/%s", basePath(), "yabba");
    var response = restTemplate.getForEntity(path, Election.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void list() throws Exception {
    // Sadly, declaring Election.options as an ImmutableList will break this.
    // It works in production code too, it's just tests that are affected. :\
    var response = restTemplate.getForEntity(basePath(), Election[].class);
    assertThat(response.getBody()).containsExactly(FRUIT_ELECTION, VEGETABLE_ELECTION);

    // Alternative way for reference.
    // var response = restTemplate.exchange(
    //     basePath(),
    //     HttpMethod.GET,
    //     null,
    //     new ParameterizedTypeReference<List<Election>>(){});
  }

  @Test
  public void vote() throws Exception {
    // Arrange
    var electionId = FRUIT_ELECTION.id();
    var path = String.format("%s/%s:vote", basePath(), electionId);
    var request = ElectionController.VoteRequest.builder().choice("tomato").build();

    // Act
    var response = restTemplate.postForEntity(path, request, Void.class);

    // Assert
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(voteStore.get(electionId))
        .containsExactly(Vote.builder().electionId(electionId).choice("tomato").build());
  }

  @Test
  public void voteTwice() throws Exception {
    // Add one vote to the store to start.
    var electionId = FRUIT_ELECTION.id();
    var firstVote = Vote.builder().electionId(electionId).choice("tomato").build();
    voteStore.put(FRUIT_ELECTION.id(), firstVote);

    // Act: vote again in the same election.
    var path = String.format("%s/%s:vote", basePath(), electionId);
    var request = ElectionController.VoteRequest.builder()
        .choice("apple")
        .choice("banana")
        .build();
    var response = restTemplate.postForEntity(path, request, Void.class);

    // Assert: Verify that the election now has two votes.
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(voteStore.get(electionId))
        .containsExactly(
            firstVote,
            Vote.builder().electionId(electionId).choice("apple").choice("banana").build());
  }

  @Test
  public void vote_invalidChoice_throwsBadRequestException() throws Exception {
    // Arrange: Vote for a choice that is not in the election config.
    var electionId = FRUIT_ELECTION.id();
    var path = String.format("%s/%s:vote", basePath(), electionId);
    var request = ElectionController.VoteRequest.builder().choice("carrot").build();

    // Act
    var response = restTemplate.postForEntity(path, request, Void.class);

    // Assert
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  public void vote_unknownElectionId_throwsBadRequestException() throws Exception {
    var electionId = "notarealid";
    var path = String.format("%s/%s:vote", basePath(), electionId);
    var request = ElectionController.VoteRequest.builder().choice("tomato").build();

    // Act
    var response = restTemplate.postForEntity(path, request, Void.class);

    // Assert
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  public void tally() {
    // Add one vote to the store to start.
    var electionId = FRUIT_ELECTION.id();
    var firstVote = Vote.builder().electionId(electionId).choice("tomato").build();
    voteStore.put(FRUIT_ELECTION.id(), firstVote);

    // Act
    var path = String.format("%s/%s:tally", basePath(), electionId);
    var response = restTemplate.postForEntity(path, null, Void.class);

    // Assert.
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_IMPLEMENTED);
  }

  private String basePath() {
    return String.format("http://localhost:%s/api/v1/elections", port);
  }

  private static final Election FRUIT_ELECTION = Election.builder()
      .id("1")
      .question("What is the best fruit?")
      .option("apple")
      .option("banana")
      .option("avocado")
      .option("tomato")
      .build();

  private static final Election VEGETABLE_ELECTION = Election.builder()
      .id("2")
      .question("What is the best vegetable?")
      .option("carrot")
      .option("broccoli")
      .option("cauliflower")
      .build();
}
