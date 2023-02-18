package dev.agaber.vote.service.converters;

import dev.agaber.vote.service.elections.Vote;
import dev.agaber.vote.service.storage.VoteDocument;

import com.google.common.base.Converter;

public class VoteVoteDocumentConverter extends Converter<Vote, VoteDocument> {

  @Override
  protected VoteDocument doForward(Vote vote) {
    return VoteDocument.builder()
        .objectId(Converters.toObjectId(vote.getId()))
        .electionObjectId(Converters.toObjectId(vote.getElectionId()))
        .choices(vote.getChoices())
        .build();
  }

  @Override
  protected Vote doBackward(VoteDocument document) {
    return Vote.builder()
        .id(Converters.toId(document.getObjectId()))
        .electionId(Converters.toId(document.getElectionObjectId()))
        .choices(document.getChoices())
        .build();
  }
}
