package dev.agaber.vote.service.elections;

import static org.assertj.core.api.Assertions.assertThat;

import dev.agaber.vote.service.server.VoteServiceApplication;
import dev.agaber.vote.service.elections.ElectionService.ElectionStore;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/** Starts a local server and communicates with actual HTTP REST. */
@ContextConfiguration(classes = {VoteServiceApplication.class, ElectionConfiguration.class})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
final class ElectionsIntegrationTest {
  @Inject @ElectionStore private Map<String, Election> electionStore;
  @Inject private TestRestTemplate restTemplate;
  @Value(value = "${local.server.port}") private int port;

  @Test
  public void listShouldReturnAllElectionsInElectionStore() throws Exception {
    var e1 = Election.builder()
        .id("1")
        .question("What is the best fruit?")
        .options(ImmutableList.of("apple", "banana", "avocado", "tomato"))
        .build();
    var e2 = Election.builder()
        .id("2")
        .question("What is the best vegetable?")
        .options(ImmutableList.of("carrot", "broccoli", "cauliflower"))
        .build();
    electionStore.put(e1.id(), e1);
    electionStore.put(e2.id(), e2);

    // Sadly, declaring Election.options as an ImmutableList will break this.
    // It works in production code too, it's just tests that are affected. :\
    var response = restTemplate.getForEntity(basePath(), Election[].class);
    assertThat(response.getBody()).containsExactly(e1, e2);

    // Alternative way.
    // var response = restTemplate.exchange(
    //     basePath(),
    //     HttpMethod.GET,
    //     null,
    //     new ParameterizedTypeReference<List<Election>>(){});
  }

  private String basePath() {
    return String.format("http://localhost:%s/api/v1/elections", port);
  }
}
