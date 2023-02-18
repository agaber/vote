package dev.agaber.vote.service.storage;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Represents a Vote document in the vote service's NoSql database (MongoDB for now).
 *
 * <p>Note: We should index electObjectId because it's the main/only way this app looks up votes.
 * I don't think the Spring annotation syntax works here and I don't yet know enough yet how to
 * store a MongDB schema to text file and restore that but for now at least know any databases
 * should index the electionObjectId.
 */
@Data
@Document(collection = "Votes")
@Builder(toBuilder = true)
public final class VoteDocument {
  @Id
  private final ObjectId objectId;

  /** String identifying the election this vote is for. */
  @Indexed(name = "votesByElectionId")
  private final ObjectId electionObjectId;

  /**
   * The user's actual votes. The string choices must match the option strings in the parent
   * election. The order of the list implies their ranked preferences. The number of choices does
   * not necessarily need to match the number of options in the election, which is to say users
   * do not have to select every choice. However, if all choices are eliminated then their vote
   * will get lost.
   */
  @Singular
  private final List<String> choices;
}
