package dev.agaber.vote.service.elections;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

/**
 * For now an election simply consists of a question and some options for voters to choose from.
 * See https://tinyurl.com/rcv-app-prd.
 */
@Data
@Builder(toBuilder = true)
public final class Election {
  /** Unique identifier for this election. */
  private final String id;

  /** The question voters need to decide on. */
  private final String question;

  /** A list of options voters can choose from. */
  @Singular
  private final List<String> options;
}
