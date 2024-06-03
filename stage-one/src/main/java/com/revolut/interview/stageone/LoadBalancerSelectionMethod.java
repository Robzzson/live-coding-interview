package com.revolut.interview.stageone;

import java.util.List;
import java.util.Optional;

public interface LoadBalancerSelectionMethod {

  Optional<AddressEntity> select(List<AddressEntity> addressEntityList);
}
