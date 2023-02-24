package dev.agaber.vote.service.elections;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Data about who won an election (or who is currently leading if the election is still open) and
 * how the votes were tallied.
 */
@Data
@Builder(toBuilder = true)
@Jacksonized
public class ElectionResult {
  /** Represents a voting choice and the amount of votes it received. */
  @Data
  @Builder(toBuilder = true)
  @Jacksonized
  public static class Choice {
    private boolean isEliminated;
    private final String text;
    private final int votesCounted;
  }

  /**
   * A tally of how many votes each choice received in a counting round. In an instan-runoff
   * election there can be multiple rounds of counting.
   */
  @Data
  @Builder(toBuilder = true)
  @Jacksonized
  public static class Round {
    @Singular
    private final List<Choice> counts;
  }

  /** The parent election configs. */
  private final Election election;

  /**
   * The results for each round of counting. The order of the array is reversed, so that the zero
   * element is the last voting round and the last element is the first. In traditional elections
   * there will only be one round.
   */
  @Singular
  private final List<Round> rounds;

  /** The winning choice as a string. */
  private final String winner;
}