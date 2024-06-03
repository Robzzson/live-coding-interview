package com.revolut.interview.stageone;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.inject.Provider;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RandomSelectionMethodTest {

  @BeforeEach
  void setUp() {
  }

  @Test
  void testSelect() {
    AddressEntity entity1 = new AddressEntity(UUID.randomUUID(), RandomStringUtils.randomAlphabetic(10));
    AddressEntity entity2 = new AddressEntity(UUID.randomUUID(), RandomStringUtils.randomAlphabetic(10));
    List<AddressEntity> entities = List.of(entity1, entity2);

    // TODO: use other random implementation
    Provider<Random> randomProvider = () -> new Random(1234);

    RandomSelectionMethod randomSelectionMethod = new RandomSelectionMethod(randomProvider);

    assertThat(randomSelectionMethod.select(entities))
        .get()
        .satisfies(entity -> {
          assertThat(entity).isEqualTo(entity2);
        });
    assertThat(randomSelectionMethod.select(entities))
        .get()
        .satisfies(entity -> {
          assertThat(entity).isEqualTo(entity1);
        });
    assertThat(randomSelectionMethod.select(entities))
        .get()
        .satisfies(entity -> {
          assertThat(entity).isEqualTo(entity2);
        });
    assertThat(randomSelectionMethod.select(entities))
        .get()
        .satisfies(entity -> {
          assertThat(entity).isEqualTo(entity1);
        });
  }

  @Test
  void testSelectForEmptyInput() {
    Provider<Random> randomProvider = () -> new Random(1234);

    RandomSelectionMethod randomSelectionMethod = new RandomSelectionMethod(randomProvider);

    assertThat(randomSelectionMethod.select(List.of()))
        .isEmpty();
  }
}