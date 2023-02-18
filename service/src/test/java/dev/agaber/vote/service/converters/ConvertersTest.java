package dev.agaber.vote.service.converters;

import static org.assertj.core.api.Assertions.assertThat;

import dev.agaber.vote.service.elections.Election;
import dev.agaber.vote.service.elections.Vote;
import dev.agaber.vote.service.storage.ElectionDocument;
import dev.agaber.vote.service.storage.VoteDocument;

import com.google.common.collect.ImmutableList;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

public final class ConvertersTest {
  private static final String ID = "63eff26a186d16591e8b1b31";

  private static final ObjectId OBJECT_ID = new ObjectId("63eff26a186d16591e8b1b31");

  private static final Election ELECTION = Election.builder()
      .id(ID)
      .question("what it is")
      .options(ImmutableList.of("idk", "u mad bro?", "yes"))
      .build();

  private static final ElectionDocument ELECTION_DOCUMENT = ElectionDocument.builder()
      .objectId(OBJECT_ID)
      .question(ELECTION.getQuestion())
      .options(ELECTION.getOptions())
      .build();

  private static final Vote VOTE = Vote.builder()
      .id(ID)
      .electionId(ID)
      .choices(ImmutableList.of("idk", "yes"))
      .build();

  private static final VoteDocument VOTE_DOCUMENT = VoteDocument.builder()
      .objectId(OBJECT_ID)
      .electionObjectId(OBJECT_ID)
      .choices(VOTE.getChoices())
      .build();

  @Test
  public void shouldConvertStringIdToObjectId() throws Exception {
    assertThat(Converters.toObjectId(ID)).isEqualTo(OBJECT_ID);
  }

  @Test
  public void shouldConvertObjectIdToObject() throws Exception {
    assertThat(Converters.toId(OBJECT_ID)).isEqualTo(ID);
  }

  @Test
  public void shouldConvertElectionToElectionDocument() throws Exception {
    assertThat(Converters.toDocument(ELECTION)).isEqualTo(ELECTION_DOCUMENT);
  }

  @Test
  public void shouldConvertElectionDocumentToElection() throws Exception {
    assertThat(Converters.toResource(ELECTION_DOCUMENT)).isEqualTo(ELECTION);
  }

  @Test
  public void shouldConvertVoteToVoteDocument() throws Exception {
    assertThat(Converters.toDocument(VOTE)).isEqualTo(VOTE_DOCUMENT);
  }

  @Test
  public void shouldConvertVoteDocumentToVote() throws Exception {
    assertThat(Converters.toResource(VOTE_DOCUMENT)).isEqualTo(VOTE);
  }
}
