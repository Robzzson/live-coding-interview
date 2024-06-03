package com.revolut.interview.stageone;

import java.util.Optional;

public interface LoadBalancer {

  AddressEntity register(String address);

  Optional<AddressEntity> get();
}
