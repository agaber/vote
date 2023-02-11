package dev.agaber.vote.service.model;

import lombok.Builder;

@Builder
public record Election(String question) {
}
