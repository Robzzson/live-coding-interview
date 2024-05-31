package com.revolut.interview.stageone.lb;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.inject.Provider;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RandomSelectionStrategyTest {

  @BeforeEach
  void setUp() {
  }

  @Test
  void select() {
    Provider<Random> randomProvider = () -> new Random(1234);

    var strategy = new RandomSelectionStrategy(randomProvider);

    var id1 = UUID.randomUUID();
    var address1 = RandomStringUtils.randomAlphabetic(10);
    var id2 = UUID.randomUUID();
    var address2 = RandomStringUtils.randomAlphabetic(10);
    var mi1 = new MachineInstance(id1, address1);
    var mi2 = new MachineInstance(id2, address2);

    VavrAssertions.assertThat(strategy.select(List.of(mi1, mi2)))
        .hasValueSatisfying(mi -> {
          assertThat(mi.getId()).isEqualTo(id2);
          assertThat(mi.getAddress()).isEqualTo(address2);
        });
    VavrAssertions.assertThat(strategy.select(List.of(mi1, mi2)))
        .hasValueSatisfying(mi -> {
          assertThat(mi.getId()).isEqualTo(id1);
          assertThat(mi.getAddress()).isEqualTo(address1);
        });
    VavrAssertions.assertThat(strategy.select(List.of(mi1, mi2)))
        .hasValueSatisfying(mi -> {
          assertThat(mi.getId()).isEqualTo(id2);
          assertThat(mi.getAddress()).isEqualTo(address2);
        });
  }
}
