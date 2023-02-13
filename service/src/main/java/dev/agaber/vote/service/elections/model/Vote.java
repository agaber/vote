package dev.agaber.vote.service.elections.model;

import lombok.Builder;
import lombok.Singular;

import java.util.List;

/**
 * Represents a single vote in an election.
 *
 * @param electionId the election this vote is for.
 * @param choices    the user's actual votes. The string choices must match the option strings in
 *                   the parent election. The order of the list implies their ranked preferences.
 *                   The number of choices does not necessarily need to match the number of options
 *                   in the election, which is to say users do not have to select every choice.
 *                   However, if all choices are eliminated then their vote will get lost.
 */
@Builder(toBuilder = true)
public record Vote(String electionId, @Singular List<String> choices) {
}
