package com.revolut.interview.stageone;

import com.google.common.collect.MoreCollectors;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.lang3.NotImplementedException;

public class InMemoryLoadBalancer implements LoadBalancer {

  private final LoadBalancerProperties properties;
  private final ConcurrentLinkedQueue<MachineInstanceDto> queue;
  private final SelectionStrategy strategy;


  public InMemoryLoadBalancer(LoadBalancerProperties properties, SelectionStrategy selectionStrategy) {
    this.properties = properties;
    this.strategy = selectionStrategy;
    this.queue = new ConcurrentLinkedQueue<>(); // bad practice
  }

  @Override
  public Either<LoadBalancerException, MachineInstanceDto> registerInstance(String address) {
    if (queue.size() >= properties.getCapacity()) {
      return Either.left(new LoadBalancerException("capacity limit reached"));
    }
    UUID uuid = UUID.randomUUID();
    MachineInstanceDto dto = new MachineInstanceDto(uuid, address);
    queue.add(dto);
    return Either.right(dto);
  }

  @Override
  public Option<MachineInstanceDto> getInstance() {
    throw new NotImplementedException();
  }

  @Override
  public Collection<MachineInstanceDto> getAllInstances() {
    return queue.stream().toList();
  }

  @Override
  public Collection<MachineInstanceDto> purge() {
    ArrayList<MachineInstanceDto> instances = new ArrayList<>(queue);
    queue.removeAll(instances);
    return instances;
  }

  @Override
  public Either<LoadBalancerException, MachineInstanceDto> removeInstance(UUID instanceId) {
    Optional<MachineInstanceDto> dto = queue.stream()
        .filter(instance -> instance.getId().equals(instanceId))
        .collect(MoreCollectors.toOptional());
    return Option.ofOptional(dto)
        .toEither(() -> new LoadBalancerException("instance with id [%s] not found".formatted(instanceId)))
        .peek(queue::remove);
  }
}
