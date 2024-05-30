package com.revolut.interview.stageone;

import io.vavr.collection.Stream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class WeightedRoundRobinSelectionStrategy implements SelectionStrategy {

  final AtomicInteger counter = new AtomicInteger(0);

  final AtomicReference<Set<MachineInstanceDto>> cachedInstances = new AtomicReference<>();
  final AtomicReference<List<MachineInstanceDto>> weightedInstances = new AtomicReference<>();

  final WeightService weightService;

  public WeightedRoundRobinSelectionStrategy(WeightService weightService) {
    this.weightService = weightService;
  }

  @Override
  public Optional<MachineInstanceDto> select(List<MachineInstanceDto> instances) {
    if (instances.isEmpty()) {
      return Optional.empty();
    }

    if (cachedInstances.get() != null && cachedInstances.get().equals(Set.copyOf(instances))) {
      return getMachineInstanceDto(weightedInstances.get());
    }

    Map<MachineInstanceDto, Integer> weights = weightService.getWeights(instances);
    List<MachineInstanceDto> javaList = Stream.ofAll(weights.entrySet())
        .filter(e -> instances.contains(e.getKey()))
        .flatMap(e -> Stream.range(0, e.getValue()).map(i -> e.getKey()))
        .shuffle()
        .toJavaList();
    cachedInstances.set(new HashSet<>(instances));
    weightedInstances.set(javaList);

    return getMachineInstanceDto(weightedInstances.get());
  }

  private Optional<MachineInstanceDto> getMachineInstanceDto(List<MachineInstanceDto> instances) {
    int i = counter.accumulateAndGet(instances.size(), (current, size) -> {
      if (current >= size - 1) {
        return 0;
      } else {
        return current + 1;
      }
    });

    return Optional.of(instances.get(i));
  }
}
