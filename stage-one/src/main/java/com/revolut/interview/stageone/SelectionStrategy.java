package com.revolut.interview.stageone;

import java.util.List;
import java.util.Optional;

public interface SelectionStrategy {

  Optional<MachineInstanceDto> select(List<MachineInstanceDto> instances);
}
