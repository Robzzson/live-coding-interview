package com.revolut.interview.stageone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InMemoryLoadBalancerTest {

  static final Integer CAPACITY = 3;

  @Mock
  LoadBalancerProperties properties;

  @Mock
  SelectionStrategy selectionStrategy;

  private InMemoryLoadBalancer lb;

  @BeforeEach
  void setUp() {
    when(properties.getCapacity()).thenReturn(CAPACITY);
    this.lb = new InMemoryLoadBalancer(properties, selectionStrategy);
  }

  @Test
  void registerInstance() {
    String address = "172.31.141.232";
    VavrAssertions.assertThat(lb.registerInstance(address))
        .hasRightValueSatisfying(dto -> {
          assertThat(dto.getAddress()).isEqualTo(address);
          assertThat(dto.getId()).isNotNull();
        });
  }

  @Test
  void testRegisterInstance_capacityLimitReached() {
    var template = "xxx.xx.xxx.xxx";
    IntStream.rangeClosed(1, CAPACITY).boxed()
        .map(i -> template.replaceAll("x", i.toString()))
        .forEach(s -> VavrAssertions.assertThat(lb.registerInstance(s)).isRight());

    String address = "172.31.141.232";
    VavrAssertions.assertThat(lb.registerInstance(address))
        .hasLeftValueSatisfying(err -> {
          assertThat(err).hasMessageContaining("capacity limit reached");
        });
  }

  @Test
  void getInstance() {
    String address = "172.31.141.232";
    MachineInstanceDto dto = lb.registerInstance(address).get();

    when(selectionStrategy.select(anyList())).thenReturn(Optional.of(dto));

    VavrAssertions.assertThat(lb.getInstance())
        .contains(dto);
  }

  @Test
  void getAllInstances() {
    String address = "172.31.141.232";
    var instance = lb.registerInstance(address);

    assertThat(lb.getAllInstances())
        .containsExactly(instance.get());
  }

  @Test
  void purge() {
    var template = "xxx.xx.xxx.xxx";
    IntStream.rangeClosed(1, CAPACITY).boxed()
        .map(i -> template.replaceAll("x", i.toString()))
        .forEach(s -> VavrAssertions.assertThat(lb.registerInstance(s)).isRight());

    assertThat(lb.getAllInstances()).hasSize(3);
    assertThat(lb.purge()).hasSize(3);
    assertThat(lb.getAllInstances()).hasSize(0);
  }

  @Test
  void removeInstance() {
    String address = "172.31.141.232";
    var instance = lb.registerInstance(address).get();

    VavrAssertions.assertThat(lb.removeInstance(instance.getId()))
        .hasRightValueSatisfying(dto -> assertThat(dto).isEqualTo(instance));
  }

  @Test
  void removeInstance_instanceDoesNotExist() {
    String address = "172.31.141.232";
    lb.registerInstance(address).get();

    VavrAssertions.assertThat(lb.removeInstance(UUID.randomUUID()))
        .hasLeftValueSatisfying(err -> assertThat(err).hasMessageContaining("not found"));
  }
}