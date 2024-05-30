package com.revolut.interview.stageone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class RoundRobinSelectionStrategyTest {

  @Test
  void select() {
    RoundRobinSelectionStrategy strategy = new RoundRobinSelectionStrategy();

    var id1 = UUID.randomUUID();
    var address1 = RandomStringUtils.randomAlphabetic(10);
    var id2 = UUID.randomUUID();
    var address2 = RandomStringUtils.randomAlphabetic(10);
    var id3 = UUID.randomUUID();
    var address3 = RandomStringUtils.randomAlphabetic(10);

    MachineInstanceDto dto1 = new MachineInstanceDto(id1, address1);
    MachineInstanceDto dto2 = new MachineInstanceDto(id2, address2);
    MachineInstanceDto dto3 = new MachineInstanceDto(id3, address3);
    List<MachineInstanceDto> instances = List.of(
        dto1,
        dto2,
        dto3
    );

    assertAll(
        () -> assertThat(strategy.select(instances)).get().extracting(MachineInstanceDto::getId).isEqualTo(id2),
        () -> assertThat(strategy.select(instances)).get().extracting(MachineInstanceDto::getId).isEqualTo(id3),
        () -> assertThat(strategy.select(instances)).get().extracting(MachineInstanceDto::getId).isEqualTo(id1),
        () -> assertThat(strategy.select(instances)).get().extracting(MachineInstanceDto::getId).isEqualTo(id2),
        () -> assertThat(strategy.select(instances)).get().extracting(MachineInstanceDto::getId).isEqualTo(id3),
        () -> assertThat(strategy.select(instances)).get().extracting(MachineInstanceDto::getId).isEqualTo(id1),
        () -> assertThat(strategy.select(instances)).get().extracting(MachineInstanceDto::getId).isEqualTo(id2),
        () -> assertThat(strategy.select(List.of(dto1))).get().extracting(MachineInstanceDto::getId).isEqualTo(id1),
        () -> assertThat(strategy.select(List.of(dto3))).get().extracting(MachineInstanceDto::getId).isEqualTo(id3)
    );
  }
}
