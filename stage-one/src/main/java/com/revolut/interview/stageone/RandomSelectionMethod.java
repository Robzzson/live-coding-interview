package com.revolut.interview.stageone;

import jakarta.inject.Provider;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandomSelectionMethod implements LoadBalancerSelectionMethod {

  private final Random rnd;

  public RandomSelectionMethod(Provider<Random> rnd) {
    this.rnd = rnd.get();
  }

  @Override
  public Optional<AddressEntity> select(List<AddressEntity> addressEntityList) {
    if (addressEntityList.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(addressEntityList.get(rnd.nextInt(addressEntityList.size())));
  }
}
