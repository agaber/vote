package dev.agaber.vote.service.elections;

import dev.agaber.vote.service.elections.model.Election;

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
@RequestMapping("/api/v1/elections")
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

  /** Request body for the vote API. */
  @Builder(toBuilder = true)
  public record VoteRequest(@Singular List<String> choices) {}

  @PostMapping("/{electionId}:vote")
  public ResponseEntity vote(
      @PathVariable String electionId, @RequestBody VoteRequest voteRequest) {
    electionService.vote(electionId, ImmutableList.copyOf(voteRequest.choices));
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{electionId}:tally")
  public ResponseEntity<Election> tally(@PathVariable String electionId) {
    // TODO.
    return new ResponseEntity<Election>(HttpStatus.NOT_IMPLEMENTED);
  }
}
