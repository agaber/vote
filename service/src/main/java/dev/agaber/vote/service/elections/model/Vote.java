package dev.agaber.vote.service.elections.model;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

/** Represents a single vote in an election. */
@Data
@Builder(toBuilder = true)
public final class Vote {
  /** String identifying the election this vote is for. */
  public final String electionId;

  /**
   * The user's actual votes. The string choices must match the option strings in the parent
   * election. The order of the list implies their ranked preferences. The number of choices does
   * not necessarily need to match the number of options in the election, which is to say users
   * do not have to select every choice. However, if all choices are eliminated then their vote
   * will get lost.
   */
  @Singular
  public final List<String> choices;
}
