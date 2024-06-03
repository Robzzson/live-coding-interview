package com.revolut.interview.stageone;

public class LoadBalancerProperties {

  public LoadBalancerProperties(Integer capacity) {
    this.capacity = capacity;
  }

  final Integer capacity;

  public Integer getCapacity() {
    return capacity;
  }
}
