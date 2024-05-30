package com.revolut.interview.stageone;

import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.Collection;
import java.util.UUID;
import org.apache.commons.lang3.NotImplementedException;

public class RandomLoadBalancer implements LoadBalancer {

  private final LoadBalancerProperties properties;

  public RandomLoadBalancer(LoadBalancerProperties properties) {
    this.properties = properties;
  }

  @Override
  public Either<LoadBalancerException, MachineInstanceDto> registerInstance(String address) {
    throw new NotImplementedException();
  }

  @Override
  public Option<MachineInstanceDto> getInstance() {
    throw new NotImplementedException();
  }

  @Override
  public Collection<MachineInstanceDto> getAllInstances() {
    throw new NotImplementedException();
  }

  @Override
  public Collection<MachineInstanceDto> purge() {
    throw new NotImplementedException();
  }

  @Override
  public Either<LoadBalancerException, MachineInstanceDto> removeInstance(UUID instanceId) {
    throw new NotImplementedException();
  }
}
