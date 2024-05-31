package com.revolut.interview.stageone.lb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoundRobinSelectionStrategyTest {

  @BeforeEach
  void setUp() {
  }

  @Test
  void select() {
    var strategy = new RoundRobinSelectionStrategy();

    var id1 = UUID.randomUUID();
    var address1 = RandomStringUtils.randomAlphabetic(5);
    var id2 = UUID.randomUUID();
    var address2 = RandomStringUtils.randomAlphabetic(5);
    var id3 = UUID.randomUUID();
    var address3 = RandomStringUtils.randomAlphabetic(5);
    var mi1 = new MachineInstance(id1, address1);
    var mi2 = new MachineInstance(id2, address2);
    var mi3 = new MachineInstance(id3, address3);

    List<MachineInstance> machines = List.of(mi1, mi2, mi3);

    VavrAssertions.assertThat(strategy.select(machines))
        .hasValueSatisfying(mi -> {
          assertThat(mi).isEqualTo(mi1);
        });
    VavrAssertions.assertThat(strategy.select(machines))
        .hasValueSatisfying(mi -> {
          assertThat(mi).isEqualTo(mi2);
        });
    VavrAssertions.assertThat(strategy.select(machines))
        .hasValueSatisfying(mi -> {
          assertThat(mi).isEqualTo(mi3);
        });
    VavrAssertions.assertThat(strategy.select(machines))
        .hasValueSatisfying(mi -> {
          assertThat(mi).isEqualTo(mi1);
        });
    VavrAssertions.assertThat(strategy.select(List.of(mi1, mi2)))
        .hasValueSatisfying(mi -> {
          assertThat(mi).isEqualTo(mi2);
        });
    VavrAssertions.assertThat(strategy.select(List.of(mi1, mi2)))
        .hasValueSatisfying(mi -> {
          assertThat(mi).isEqualTo(mi1);
        });
    VavrAssertions.assertThat(strategy.select(machines))
        .hasValueSatisfying(mi -> {
          assertThat(mi).isEqualTo(mi2);
        });
    VavrAssertions.assertThat(strategy.select(machines))
        .hasValueSatisfying(mi -> {
          assertThat(mi).isEqualTo(mi3);
        });
  }
}
