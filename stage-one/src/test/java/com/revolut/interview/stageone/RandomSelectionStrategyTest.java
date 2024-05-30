package com.revolut.interview.stageone;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.inject.Provider;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RandomSelectionStrategyTest {

  @Test
  void testGetRandomInstance() {
    Provider<Random> randomProvider = () -> new Random(1234);

    RandomSelectionStrategy strategy = new RandomSelectionStrategy(randomProvider);

    var id1 = UUID.randomUUID();
    var address1 = RandomStringUtils.randomAlphabetic(10);
    var id2 = UUID.randomUUID();
    var address2 = RandomStringUtils.randomAlphabetic(10);

    List<MachineInstanceDto> instances = List.of(new MachineInstanceDto(id1, address1), new MachineInstanceDto(id2, address2));

    assertThat(strategy.select(instances))
        .get()
        .satisfies(dto -> {
          assertThat(dto.getId()).isEqualTo(id2);
          assertThat(dto.getAddress()).isEqualTo(address2);
        });
  }

  @Test
  void testGetRandomInstance_multipleExecutions() {
    Provider<Random> randomProvider = () -> new Random(1234);

    RandomSelectionStrategy strategy = new RandomSelectionStrategy(randomProvider);

    var id1 = UUID.randomUUID();
    var address1 = RandomStringUtils.randomAlphabetic(10);
    var id2 = UUID.randomUUID();
    var address2 = RandomStringUtils.randomAlphabetic(10);

    List<MachineInstanceDto> instances = List.of(new MachineInstanceDto(id1, address1), new MachineInstanceDto(id2, address2));

    List<MachineInstanceDto> results = IntStream.range(0, 100).boxed()
        .map(i -> strategy.select(instances))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();

    assertThat(results)
        .hasSize(100)
        .extracting(MachineInstanceDto::getId)
        .haveAtLeastOne(new Condition<>(id -> id.equals(id1), "first machine instance"))
        .haveAtLeastOne(new Condition<>(id -> id.equals(id2), "second machine instance"));
  }
}
