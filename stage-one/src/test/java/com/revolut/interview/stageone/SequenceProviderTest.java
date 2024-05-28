package com.revolut.interview.stageone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SequenceProviderTest {

  SequenceProvider sequenceProvider;

  @BeforeEach
  void setUp() {
    this.sequenceProvider = new SequenceProvider();
  }

  @Test
  void testGetNext() {
    assertAll(
        () -> assertThat(sequenceProvider.getNext()).isEqualTo(0),
        () -> assertThat(sequenceProvider.getNext()).isEqualTo(1),
        () -> assertThat(sequenceProvider.getNext()).isEqualTo(2),
        () -> assertThat(sequenceProvider.getNext()).isEqualTo(3),
        () -> assertThat(sequenceProvider.getNext()).isEqualTo(4),
        () -> assertThat(sequenceProvider.getNext()).isEqualTo(5)
    );
  }
}