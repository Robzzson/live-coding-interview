package com.revolut.interview.stageone.lb;

import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.lang3.NotImplementedException;

public class InMemoryLoadBalancer implements LoadBalancer {

  private final LoadBalancerProperties properties;
  private final LoadBalancerSelectionStrategy selectionStrategy;
  ConcurrentLinkedQueue<MachineInstance> queue;

  public InMemoryLoadBalancer(LoadBalancerProperties properties, LoadBalancerSelectionStrategy selectionStrategy) {
    this.properties = properties;
    this.selectionStrategy = selectionStrategy;
    this.queue = new ConcurrentLinkedQueue<>(); // bad practice
  }

  @Override
  public Either<LoadBalancerException, MachineInstance> registerInstance(String address) {
    throw new NotImplementedException();
  }

  @Override
  public Option<MachineInstance> getInstance() {
    throw new NotImplementedException();
  }

  @Override
  public Collection<MachineInstance> getAllInstances() {
    throw new NotImplementedException();
  }
}
