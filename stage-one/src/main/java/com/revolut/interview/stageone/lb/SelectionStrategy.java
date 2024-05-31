package com.revolut.interview.stageone.lb;

import io.vavr.control.Option;
import java.util.List;

public interface SelectionStrategy {

  Option<MachineInstance> select(List<MachineInstance> machines);
}
