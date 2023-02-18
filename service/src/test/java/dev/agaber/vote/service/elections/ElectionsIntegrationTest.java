package dev.agaber.vote.service.elections;

import static org.assertj.core.api.Assertions.assertThat;

import dev.agaber.vote.service.converters.Converters;
import dev.agaber.vote.service.server.VoteServiceApplication;
import dev.agaber.vote.service.storage.ElectionDocument;
import dev.agaber.vote.service.storage.VoteDocument;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.ImmutableList;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/** Starts a local server and communicates with actual HTTP REST. */
@AutoConfigureDataMongo
@ContextConfiguration(classes = VoteServiceApplication.class)
@SpringBootTest(
    properties = "de.flapdoodle.mongodb.embedded.version=5.0.5",
    webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext
final class ElectionsIntegrationTest {
  @Inject
  private MongoTemplate mongoTemplate;

  @Inject
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int port = 8085;

  @Inject
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() throws Exception {
    mongoTemplate.dropCollection("Elections");
    mongoTemplate.dropCollection("Votes");
    mongoTemplate.insert(FRUIT_ELECTION, "Elections");
    mongoTemplate.insert(VEGETABLE_ELECTION, "Elections");
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
    var expected = newElection.toBuilder().id(response.getId()).build();
    assertThat(response).isEqualTo(expected);

    var objectId = Converters.toObjectId(response.getId());
    var saved = Converters.toResource(mongoTemplate.findById(objectId, ElectionDocument.class));
    assertThat(saved).isEqualTo(expected);
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
    var path = String.format("%s/%s", basePath(), FRUIT_ELECTION.getId());
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
    var electionId = FRUIT_ELECTION.getId();
    var path = String.format("%s/%s:vote", basePath(), electionId);
    var request = ElectionController.VoteRequest.builder().choice("tomato").build();

    // Act
    var response = restTemplate.postForEntity(path, request, Vote.class);

    // Assert
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    var expected = Vote.builder()
        .id(response.getBody().getId())
        .electionId(FRUIT_ELECTION.getId())
        .choice("tomato")
        .build();
    assertThat(response.getBody()).isEqualTo(expected);

    var objectId = Converters.toObjectId(response.getBody().getId());
    var saved = Converters.toResource(mongoTemplate.findById(objectId, VoteDocument.class));
    assertThat(saved).isEqualTo(expected);
  }

  @Test
  public void voteTwice() throws Exception {
    // Add one vote to the store to start.
    var electionId = FRUIT_ELECTION.getId();
    var firstVote = Vote.builder()
        .id("63f00f05a5050bd4babf3de3")
        .electionId(electionId)
        .choice("tomato")
        .build();
    var expectedFirstVoteDocument = Converters.toDocument(firstVote);
    mongoTemplate.insert(expectedFirstVoteDocument, "Votes");

    // Act: vote again in the same election.
    var path = String.format("%s/%s:vote", basePath(), electionId);
    var request = ElectionController.VoteRequest.builder()
        .choice("apple")
        .choice("banana")
        .build();
    var response = restTemplate.postForEntity(path, request, Vote.class);

    // Assert: Verify that the election now has two votes.
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<VoteDocument> foundVotes = findVotesByElectionId(electionId);

    var expectedSecondVoteDocument = VoteDocument.builder()
        .objectId(Converters.toObjectId(response.getBody().getId()))
        .electionObjectId(expectedFirstVoteDocument.getElectionObjectId())
        .choice("apple")
        .choice("banana")
        .build();
    assertThat(foundVotes).containsExactly(expectedFirstVoteDocument, expectedSecondVoteDocument);
  }

  @Test
  public void vote_invalidChoice_throwsBadRequestException() throws Exception {
    // Arrange: Vote for a choice that is not in the election config.
    var electionId = FRUIT_ELECTION.getId();
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

  private static final class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {
    @Override
    public ObjectId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      JsonNode oid = ((JsonNode) p.readValueAsTree()).get("$oid");
      return new ObjectId(oid.asText());
    }
  }

  /**
   * Tests that votes are counted and winner is determined as expected.
   *
   * <p>What I'm trying to simulate in this test:
   *
   * <pre>
   *   | choice  | round 1 | round 2 | round 3 | round 4 |
   *   |---------|---------|---------|---------|---------|
   *   | apple   | 5%      | -       | -       | -       |
   *   | banana  | 25%     | 25%     | 25%     | -       |
   *   | avocado | 25%     | 30%     | 30%     | 55%     |
   *   | tomato  | 30%     | 30%     | 45%     | 45%     |
   *   | orange  | 15%     | 15%     | -       | -       |
   * </pre>
   */
  @Test
  public void tally() throws Exception {
    // Arrange: load the database with saved data described in the javadoc.
    var electionId = Converters.toObjectId(FRUIT_ELECTION.getId());
    var votes = parseVotes(read("tally_fruit_election.json"));
    votes.stream().forEach(v -> mongoTemplate.insert(v, "Votes"));

    // Act
    var path = String.format("%s/%s:tally", basePath(), electionId);
    var response = restTemplate.postForEntity(path, null, String.class);

    // Assert.
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo("avocado");
  }

  @Test
  public void tally_simpleMajority() {
    var electionId = LUNCH_ELECTION_DOCUMENT.getObjectId();
    mongoTemplate.insert(LUNCH_ELECTION_DOCUMENT, "Elections");

    mongoTemplate.insert(
        VoteDocument.builder()
            .electionObjectId(electionId)
            .choice("sandwich")
            .build(),
        "Votes");

    mongoTemplate.insert(
        VoteDocument.builder()
            .electionObjectId(electionId)
            .choice("pizza")
            .build(),
        "Votes");

    mongoTemplate.insert(
        VoteDocument.builder()
            .electionObjectId(electionId)
            .choice("pizza")
            .build(),
        "Votes");

    // Act
    var path = String.format("%s/%s:tally", basePath(), electionId);
    var result = restTemplate.postForObject(path, null, String.class);

    // Assert.
    assertThat(result).isEqualTo("pizza");
  }

  @Test
  public void tally_onevote() {
    var electionId = LUNCH_ELECTION_DOCUMENT.getObjectId();
    mongoTemplate.insert(LUNCH_ELECTION_DOCUMENT, "Elections");

    mongoTemplate.insert(
        VoteDocument.builder()
            .electionObjectId(electionId)
            .choice("sandwich")
            .build(),
        "Votes");

    // Act
    var path = String.format("%s/%s:tally", basePath(), electionId);
    var result = restTemplate.postForObject(path, null, String.class);

    // Assert.
    assertThat(result).isEqualTo("sandwich");
  }

  @Test
  public void tally_tie() {
    var electionId = LUNCH_ELECTION_DOCUMENT.getObjectId();
    mongoTemplate.insert(LUNCH_ELECTION_DOCUMENT, "Elections");

    mongoTemplate.insert(
        VoteDocument.builder()
            .electionObjectId(electionId)
            .choice("sandwich")
            .build(),
        "Votes");

    mongoTemplate.insert(
        VoteDocument.builder()
            .electionObjectId(electionId)
            .choice("pizza")
            .build(),
        "Votes");

    // Act
    var path = String.format("%s/%s:tally", basePath(), electionId);
    var result = restTemplate.postForObject(path, null, String.class);

    // Assert - Expect it to take the first one.
    assertThat(result).isEqualTo("sandwich");
  }

  @Test
  public void tally_novotes() {
    var electionId = LUNCH_ELECTION_DOCUMENT.getObjectId();
    mongoTemplate.insert(LUNCH_ELECTION_DOCUMENT, "Elections");

    // Act
    var path = String.format("%s/%s:tally", basePath(), electionId);
    var response = restTemplate.postForEntity(path, null, String.class);

    // Assert - Expect it to return empty response (no winner).
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNull();
  }

  private String basePath() {
    return String.format("http://localhost:%s/vote/api/v1/elections", port);
  }

  private List<VoteDocument> findVotesByElectionId(String electionId) {
    var query = new Query();
    query.addCriteria(Criteria.where("electionObjectId").is(Converters.toObjectId(electionId)));
    var foundVotes = mongoTemplate.find(query, VoteDocument.class, "Votes");
    return foundVotes;
  }

  private ImmutableList<VoteDocument> parseVotes(String json) throws JsonProcessingException {
    var objIdMod = new SimpleModule("ObjectId", new Version(1, 0, 0, null, null, null));
    objIdMod.addDeserializer(ObjectId.class, new ObjectIdDeserializer());
    objectMapper.registerModule(objIdMod);
    return ImmutableList.copyOf(
        objectMapper.readValue(json, new TypeReference<List<VoteDocument>>() {
        }));
  }

  private static String read(String filename) throws IOException, URISyntaxException {
    var clazz = ElectionsIntegrationTest.class;
    var path = String.format("%s/%s",
        clazz.getPackageName().replace(".", "/"),
        filename);
    var resource = clazz.getClassLoader().getResource(path);
    return Files.readString(new File(resource.toURI()).toPath(), StandardCharsets.UTF_8);
  }

  private static final Election FRUIT_ELECTION = Election.builder()
      .question("What is the best fruit?")
      .id("63f00c68a5050bd4babf3de1")
      .option("apple")
      .option("banana")
      .option("avocado")
      .option("tomato")
      .option("orange")
      .build();

  private static final Election VEGETABLE_ELECTION = Election.builder()
      .question("What is the best vegetable?")
      .id("63f00c83a5050bd4babf3de2")
      .option("carrot")
      .option("broccoli")
      .option("cauliflower")
      .build();

  private static final ElectionDocument LUNCH_ELECTION_DOCUMENT = ElectionDocument.builder()
      .objectId(Converters.toObjectId("63f02d40a5050bd4babf3df4"))
      .question("What do you want for lunch?")
      .options(ImmutableList.of("sandwich", "pizza", "nothing", "fruit"))
      .build();
}
