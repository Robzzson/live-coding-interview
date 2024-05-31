package com.revolut.interview.stageone.lb;

import io.vavr.control.Option;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinSelectionStrategy implements SelectionStrategy {

  final AtomicInteger count = new AtomicInteger(-1);

  @Override
  public Option<MachineInstance> select(List<MachineInstance> machines) {
    if (machines.isEmpty()) {
      return Option.none();
    }

    int i = count.accumulateAndGet(machines.size(), (curr, size) -> {
      if (curr >= size - 1) {
        return 0;
      } else {
        return curr + 1;
      }
    });

    return Option.of(machines.get(i));
  }
}
