package dev.agaber.vote.service.storage;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/** A data access object for Elections stored in the NoSql database. */
@Repository
public interface ElectionRepository extends MongoRepository<ElectionDocument, ObjectId> {
}
