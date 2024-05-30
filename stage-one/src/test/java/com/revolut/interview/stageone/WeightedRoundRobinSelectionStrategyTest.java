package com.revolut.interview.stageone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WeightedRoundRobinSelectionStrategyTest {

  @Mock
  WeightService weightService;

  @Test
  void select() {
    WeightedRoundRobinSelectionStrategy strategy = new WeightedRoundRobinSelectionStrategy(weightService);

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

    when(weightService.getWeights(anyList()))
        .thenReturn(Map.of(
            dto1, 5,
            dto2, 3,
            dto3, 2
        ));

    List<MachineInstanceDto> list = IntStream.range(0, 1000).boxed().map(i -> strategy.select(instances))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();

    assertThat(list)
        .hasSize(1000)
        .extracting(MachineInstanceDto::getId)
        .haveExactly(500, new Condition<>(id -> id.equals(id1), "first machine instance"))
        .haveExactly(300, new Condition<>(id -> id.equals(id2), "second machine instance"))
        .haveExactly(200, new Condition<>(id -> id.equals(id3), "third machine instance"));

    List<MachineInstanceDto> updatedList = IntStream.range(0, 1000).boxed().map(i -> strategy.select(List.of(dto2, dto3)))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();

    assertThat(updatedList)
        .hasSize(1000)
        .extracting(MachineInstanceDto::getId)
        .doNotHave(new Condition<>(id -> id.equals(id1), "first machine instance"))
        .haveExactly(600, new Condition<>(id -> id.equals(id2), "second machine instance"))
        .haveExactly(400, new Condition<>(id -> id.equals(id3), "third machine instance"));
  }
}