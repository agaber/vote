package dev.agaber.vote.service.converters;

import dev.agaber.vote.service.elections.Election;
import dev.agaber.vote.service.elections.Vote;
import dev.agaber.vote.service.storage.ElectionDocument;
import dev.agaber.vote.service.storage.VoteDocument;

import org.bson.types.ObjectId;

/**
 * Converts to/from API resource objects and database Document objects.
 *
 * <p>There's probably a more clever Springy way to deal with this but...
 *
 * <ul>
 *   <li>strongly prefer exposing a single string ID in the API rather than an
 *   ObjectID which may be vendor specific and leaks implementation details out
 *   of the API and anyway...
 *   <li>it's generally good practice to separate API objects from database
 *   objects so there's that. :\
 * </ul>
 */
public final class Converters {
  private Converters() {}

  public static Election toResource(ElectionDocument document) {
    return ELECTION_CONVERTER.reverse().convert(document);
  }

  public static Vote toResource(VoteDocument document) {
    return VOTE_CONVERTER.reverse().convert(document);
  }

  public static ElectionDocument toDocument(Election election) {
    return ELECTION_CONVERTER.convert(election);
  }

  public static VoteDocument toDocument(Vote vote) {
    return VOTE_CONVERTER.convert(vote);
  }

  public static String toId(ObjectId objectId) {
    return OBJECTID_CONVERTER.reverse().convert(objectId);
  }

  public static ObjectId toObjectId(String id) {
    return OBJECTID_CONVERTER.convert(id);
  }

  private static final ElectionElectionDocumentConverter ELECTION_CONVERTER =
      new ElectionElectionDocumentConverter();

  private static final ObjectIdConverter OBJECTID_CONVERTER =
      new ObjectIdConverter();

  private static final VoteVoteDocumentConverter VOTE_CONVERTER =
      new VoteVoteDocumentConverter();
}
