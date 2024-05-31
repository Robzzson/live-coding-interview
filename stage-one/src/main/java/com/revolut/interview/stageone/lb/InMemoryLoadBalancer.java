package com.revolut.interview.stageone.lb;

import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

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
  public synchronized Either<LoadBalancerException, MachineInstance> registerInstance(String address) {
    if (queue.size() >= properties.getCapacity()) {
      return Either.left(new LoadBalancerException("capacity limit reached"));
    }
    var id = UUID.randomUUID();
    var mi = new MachineInstance(id, address);
    queue.add(mi);
    return Either.right(mi);
  }

  @Override
  public Option<MachineInstance> getInstance() {
    return selectionStrategy.select(queue.stream().toList());
  }

  @Override
  public Collection<MachineInstance> getAllInstances() {
    return queue.stream().toList();
  }
}
