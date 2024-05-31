package com.revolut.interview.stageone.lb;

import io.vavr.control.Option;
import java.util.List;

public interface LoadBalancerSelectionStrategy {

  Option<MachineInstance> select(List<MachineInstance> machines);
}
