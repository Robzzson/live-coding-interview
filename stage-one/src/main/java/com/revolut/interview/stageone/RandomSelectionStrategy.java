package com.revolut.interview.stageone;

import jakarta.inject.Provider;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandomSelectionStrategy implements SelectionStrategy {

  private final Random rnd;

  public RandomSelectionStrategy(Provider<Random> rnd) {
    this.rnd = rnd.get();
  }

  @Override
  public Optional<MachineInstanceDto> select(List<MachineInstanceDto> instances) {
    if (instances.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(instances.get(rnd.nextInt(instances.size())));
  }
}
