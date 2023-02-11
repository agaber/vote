package dev.agaber.vote.service.restapi;

import dev.agaber.vote.service.model.Election;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.ImmutableList;

import java.util.List;

@RestController
@RequestMapping("/api/v1/election")
public class ElectionController {
  @GetMapping
  public ResponseEntity<List<Election>> getAllElections() {
    var elections = ImmutableList.of(new Election("whatever"));
    return new ResponseEntity<List<Election>>(elections, HttpStatus.OK);
  }
}
