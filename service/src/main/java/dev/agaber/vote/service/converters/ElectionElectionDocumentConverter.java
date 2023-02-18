package dev.agaber.vote.service.converters;

import dev.agaber.vote.service.elections.Election;
import dev.agaber.vote.service.storage.ElectionDocument;

import com.google.common.base.Converter;

public final class ElectionElectionDocumentConverter extends Converter<Election, ElectionDocument> {
  private static final ElectionElectionDocumentConverter INSTANCE =
      new ElectionElectionDocumentConverter();

  public static ElectionElectionDocumentConverter getInstance() {
    return INSTANCE;
  }

  @Override
  protected ElectionDocument doForward(Election election) {
    return ElectionDocument.builder()
        .objectId(Converters.toObjectId(election.getId()))
        .question(election.getQuestion())
        .options(election.getOptions())
        .build();
  }

  @Override
  protected Election doBackward(ElectionDocument document) {
    return Election.builder()
        .id(Converters.toId(document.getObjectId()))
        .question(document.getQuestion())
        .options(document.getOptions())
        .build();
  }
}
