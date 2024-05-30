package com.revolut.interview.stageone;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class BasicModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(SequenceProvider.class).in(Scopes.SINGLETON);
  }
}
