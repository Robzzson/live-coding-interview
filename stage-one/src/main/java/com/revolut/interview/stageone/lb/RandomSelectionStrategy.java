package com.revolut.interview.stageone.lb;

import com.google.inject.Provider;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import java.util.List;
import java.util.Random;

public class RandomSelectionStrategy implements SelectionStrategy {

  private final Random random;

  public RandomSelectionStrategy(Provider<Random> randomProvider) {
    this.random = randomProvider.get();
  }

  @Override
  public Option<MachineInstance> select(List<MachineInstance> machines) {
    if (machines.isEmpty()) {
      return Option.none();
    }
    return Option.of(machines.get(random.nextInt(machines.size())));
  }
}
