package com.revolut.interview.stageone.lb;

import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.Collection;

public interface LoadBalancer {

  Either<LoadBalancerException, MachineInstance> registerInstance(String address);

  Option<MachineInstance> getInstance();

  Collection<MachineInstance> getAllInstances();
}
