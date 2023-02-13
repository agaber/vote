package dev.agaber.vote.service.elections.inject;

import java.lang.annotation.RetentionPolicy;

public final class Annotations {
  /** Injection annotation for the election store Map. */
  @java.lang.annotation.Documented
  @java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
  @javax.inject.Qualifier
  public @interface ElectionStore {}

  /** Injection annotation for the vote store Map. */
  @java.lang.annotation.Documented
  @java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
  @javax.inject.Qualifier
  public @interface VoteStore {}

  private Annotations() {}
}
