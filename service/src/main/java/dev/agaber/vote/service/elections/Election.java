package dev.agaber.vote.service.elections;

import com.google.common.collect.ImmutableList;
import lombok.Builder;

import java.util.List;

/**
 * For now an election simply consists of a question and some options for voters to choose from.
 * See https://tinyurl.com/rcv-app-prd.
 *
 * @param id       unique identifier for this election.
 * @param question the thing voters need to decide on.
 * @param options  a list of options voters can choose from.
 */
@Builder
record Election(String id, String question, List<String> options) {
}
