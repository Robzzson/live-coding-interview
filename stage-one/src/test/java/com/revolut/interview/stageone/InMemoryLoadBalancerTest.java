package com.revolut.interview.stageone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InMemoryLoadBalancerTest {

  private InMemoryLoadBalancer lb;

  @Mock
  LoadBalancerSelectionMethod selectionMethod;

  @BeforeEach
  void setUp() {
  }

  @Test
  void testRegister() {
    LoadBalancerProperties loadBalancerProperties = new LoadBalancerProperties(10);
    this.lb = new InMemoryLoadBalancer(loadBalancerProperties, selectionMethod);

    String address = RandomStringUtils.randomAlphabetic(10);
    assertThat(lb.register(address))
        .satisfies(addressEntity -> {
          assertThat(addressEntity.getId()).isNotNull();
          assertThat(addressEntity.getAddress()).isEqualTo(address);
        });
  }

  @Test
  void testRegister_capacityExceeded() {
    LoadBalancerProperties loadBalancerProperties = new LoadBalancerProperties(1);
    this.lb = new InMemoryLoadBalancer(loadBalancerProperties, selectionMethod);

    String address = RandomStringUtils.randomAlphabetic(10);

    lb.register(address);

    assertThatThrownBy(() -> lb.register(RandomStringUtils.randomAlphabetic(10)))
        .isInstanceOfSatisfying(LoadBalancerException.class, exc -> {
          assertThat(exc.getMessage()).contains("capacity limit reached");
        });
  }

  @Test
  void testRegister_DuplicateError() {
    LoadBalancerProperties loadBalancerProperties = new LoadBalancerProperties(10);
    this.lb = new InMemoryLoadBalancer(loadBalancerProperties, selectionMethod);

    String address = RandomStringUtils.randomAlphabetic(10);

    lb.register(address);

    assertThatThrownBy(() -> lb.register(address))
        .isInstanceOfSatisfying(LoadBalancerException.class, exc -> {
          assertThat(exc.getMessage()).contains("duplicate");
        });
  }

  @Test
  void testGet_byRandomSelectionMethod() {
    LoadBalancerProperties loadBalancerProperties = new LoadBalancerProperties(10);

    this.lb = new InMemoryLoadBalancer(loadBalancerProperties, selectionMethod);

    AddressEntity entity = this.lb.register(RandomStringUtils.randomAlphabetic(10));

    when(selectionMethod.select(anyList()))
        .thenReturn(Optional.of(entity));

    assertThat(lb.get())
        .get()
        .satisfies(ae -> {
          assertThat(ae).isEqualTo(entity);
        });
  }
}
