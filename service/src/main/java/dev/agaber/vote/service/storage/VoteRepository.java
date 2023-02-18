package dev.agaber.vote.service.storage;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** A data access object for Votes stored in the NoSql database. */
@Repository
public interface VoteRepository extends MongoRepository<VoteDocument, ObjectId> {
  List<VoteDocument> findAllByElectionObjectId(ObjectId electionObjectId);
}
