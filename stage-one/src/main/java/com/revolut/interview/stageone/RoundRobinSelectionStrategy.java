package com.revolut.interview.stageone;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinSelectionStrategy implements SelectionStrategy {

  final AtomicInteger counter = new AtomicInteger(0);
  
  @Override
  public Optional<MachineInstanceDto> select(List<MachineInstanceDto> instances) {
    if (instances.isEmpty()) {
      return Optional.empty();
    }

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
