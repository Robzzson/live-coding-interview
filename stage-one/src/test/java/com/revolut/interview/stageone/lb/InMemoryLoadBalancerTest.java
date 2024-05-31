package com.revolut.interview.stageone.lb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import jakarta.inject.Provider;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InMemoryLoadBalancerTest {

  @Mock
  LoadBalancerProperties properties;

  @Mock
  LoadBalancerSelectionStrategy selectionStrategy;

  final Provider<String> randomStringProvider = () -> RandomStringUtils.randomAlphabetic(10);

  private InMemoryLoadBalancer lb;

  @BeforeEach
  void setUp() {
    this.lb = new InMemoryLoadBalancer(properties, selectionStrategy);
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 5, 10})
  void registerInstance(Integer numberOfInstances) {
    when(properties.getCapacity())
        .thenReturn(10);

    IntStream.range(0, numberOfInstances).boxed()
        .map(i -> randomStringProvider.get())
        .forEach(address -> VavrAssertions.assertThat(lb.registerInstance(address))
            .hasRightValueSatisfying(mi -> {
              assertThat(mi.getId()).isNotNull();
              assertThat(mi.getAddress()).isNotBlank();
            }));
  }

  @Test
  void registerInstance_capacityExceeded() {
    when(properties.getCapacity())
        .thenReturn(1);

    lb.registerInstance(RandomStringUtils.randomAlphabetic(3));

    VavrAssertions.assertThat(lb.registerInstance(randomStringProvider.get()))
        .hasLeftValueSatisfying(err -> {
          assertThat(err).hasMessageContaining("capacity limit reached");
        });
  }

  @Test
  void getInstance() {

  }

  @Test
  void getAllInstances() {
    var instances = List.of(
        new MachineInstance(UUID.randomUUID(), randomStringProvider.get()),
        new MachineInstance(UUID.randomUUID(), randomStringProvider.get()),
        new MachineInstance(UUID.randomUUID(), randomStringProvider.get())
    );
    lb.queue.addAll(instances);

    assertThat(lb.getAllInstances())
        .hasSize(3);
  }
}