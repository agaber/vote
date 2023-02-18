package dev.agaber.vote.service.storage;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/** Represents an Election document in the vote service's NoSql database (MongoDB for now). */
@Data
@Document(collection = "Elections")
@Builder(toBuilder = true)
public final class ElectionDocument {
  /** Unique identifier for this election. */
  @Id
  private final ObjectId objectId;

  /** The question voters need to decide on. */
  private final String question;

  /** A list of options voters can choose from. */
  @Singular
  private final List<String> options;
}
