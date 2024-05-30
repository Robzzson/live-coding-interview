package com.revolut.interview.stageone;

import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.Collection;
import java.util.UUID;

public interface LoadBalancer {

  Either<LoadBalancerException, MachineInstanceDto> registerInstance(String address);

  Option<MachineInstanceDto> getInstance();

  Collection<MachineInstanceDto> getAllInstances();

  Collection<MachineInstanceDto> purge();

  Either<LoadBalancerException, MachineInstanceDto> removeInstance(UUID instanceId);
}
