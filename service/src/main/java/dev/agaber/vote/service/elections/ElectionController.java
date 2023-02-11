package dev.agaber.vote.service.elections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Defines the public REST API for the elections resource. The controller is the first code
 * executed when an API method is called.
 */
@RestController
@RequestMapping("/api/v1/elections")
final class ElectionController {
  private final ElectionService electionService;

  @Autowired
  ElectionController(ElectionService electionService) {
    this.electionService = electionService;
  }

  @GetMapping
  public ResponseEntity<List<Election>> list() {
    return new ResponseEntity<List<Election>>(electionService.listElections(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Election> getById() {
    return new ResponseEntity<Election>(HttpStatus.NOT_IMPLEMENTED);
  }

  @PostMapping
  public ResponseEntity<Election> create() {
    return new ResponseEntity<Election>(HttpStatus.NOT_IMPLEMENTED);
  }

  @PatchMapping
  public ResponseEntity<Election> update() {
    return new ResponseEntity<Election>(HttpStatus.NOT_IMPLEMENTED);
  }

  @PostMapping("/{id}:vote")
  public ResponseEntity<Election> vote() {
    return new ResponseEntity<Election>(HttpStatus.NOT_IMPLEMENTED);
  }

  @PostMapping("/{id}:tally")
  public ResponseEntity<Election> tally() {
    return new ResponseEntity<Election>(HttpStatus.NOT_IMPLEMENTED);
  }
}
