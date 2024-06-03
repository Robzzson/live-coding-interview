package com.revolut.interview.stageone;

import com.google.common.collect.MoreCollectors;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

public class InMemoryLoadBalancer implements LoadBalancer {

  final ConcurrentLinkedQueue<AddressEntity> queue = new ConcurrentLinkedQueue<>();
  final ReentrantLock lock = new ReentrantLock();

  private final LoadBalancerProperties properties;
  private final LoadBalancerSelectionMethod selectionMethod;

  public InMemoryLoadBalancer(LoadBalancerProperties properties, LoadBalancerSelectionMethod selectionMethod) {
    this.properties = properties;
    this.selectionMethod = selectionMethod;
  }

  @Override
  public AddressEntity register(String address) {
    lock.lock();
    try {
      if (queue.size() >= properties.getCapacity()) {
        throw new LoadBalancerException("capacity limit reached");
      }
      if (queue.stream().filter(ae -> ae.getAddress().equals(address)).collect(MoreCollectors.toOptional()).isPresent()) {
        throw new LoadBalancerException("duplicate found");
      }

      var uuid = UUID.randomUUID();
      AddressEntity addressEntity = new AddressEntity(uuid, address);
      queue.add(addressEntity);
      return addressEntity;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public Optional<AddressEntity> get() {
    return selectionMethod.select(queue.stream().toList());
  }
}
