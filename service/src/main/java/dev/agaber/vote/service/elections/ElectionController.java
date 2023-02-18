package dev.agaber.vote.service.elections;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Singular;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * Defines the public REST API for the elections resource. The controller is the first code
 * executed when an API method is called.
 */
@RestController
@RequestMapping("/vote/api/v1/elections")
final class ElectionController {
  private final ElectionService electionService;

  @Inject
  ElectionController(ElectionService electionService) {
    this.electionService = electionService;
  }

  @PostMapping
  public ResponseEntity<Election> create(@RequestBody Election election) {
    var createdElection = electionService.createElection(election);
    return ResponseEntity.ok(createdElection);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Election> getById(@PathVariable String id) {
    return electionService.getById(id)
        .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping
  public ResponseEntity<List<Election>> list() {
    return ResponseEntity.ok(electionService.listElections());
  }

  /** Does not allow updates to an election once created. */
  @PatchMapping
  public ResponseEntity<Election> update() {
    // TODO: add an isDraft field (or status field) to election and only allow updates to drafts.
    // Draft status cannot be changed once published.
    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
  }

  /**
   * Request body for the vote API.
   *
   * @param choices the things the user is voting on. The choices must match the options in the
   *                parent election. The order of the input implies the user's preference in the
   *                election. Meaning the choice at index 0 is their first choice and index 1 is
   *                their second choice, and so on. Voters do not have to include all options in
   *                their choices, but if all of their choices are eliminated their vote will not
   *                be counted. Users shouldn't vote for the same choice more than once but there
   *                is nothing validating that yet (there might be eventually though).
   */
  @Builder(toBuilder = true)
  public record VoteRequest(@Singular List<String> choices) {}

  @PostMapping("/{electionId}:vote")
  public ResponseEntity vote(
      @PathVariable String electionId, @RequestBody VoteRequest voteRequest) {
    var vote = electionService.vote(electionId, ImmutableList.copyOf(voteRequest.choices));
    return ResponseEntity.ok(vote);
  }

  /**
   * Returns the winner of an election at the time the API was called using the
   * <a href="https://en.wikipedia.org/wiki/Instant-runoff_voting">instant runoff</a>
   * vote counting strategy.
   *
   * <p>An example election:
   *
   * <pre>
   *   | choice  | round 1 | round 2 | round 3 | round 4 |
   *   |---------|---------|---------|---------|---------|
   *   | tomato  | 5%      | -       | -       | -       |
   *   | banana  | 25%     | 25%     | 25%     | -       |
   *   | avocado | 25%     | 30%     | 30%     | 55%     |
   *   | orange  | 30%     | 30%     | 45%     | 45%     |
   *   | apple   | 15%     | 15%     | -       | -       |
   *
   *   Winner: avocado! Of course, it's a superfood!
   * </pre>
   */
  @PostMapping("/{electionId}:tally")
  public ResponseEntity<String> tally(@PathVariable String electionId) {
    return ResponseEntity.ok(electionService.tally(electionId));
  }
}
