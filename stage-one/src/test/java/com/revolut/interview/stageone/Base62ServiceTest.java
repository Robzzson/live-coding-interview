package com.revolut.interview.stageone;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class Base62ServiceTest {

  Base62Service service = new Base62Service();

  @ParameterizedTest
  @CsvSource({
      "125, cb",
      "86203, wAx",
      "0, a",
      "1, b"
  })
  void testToBase62(String number, String expectedValue) {
    assertThat(service.encode(Double.valueOf(number)))
        .isEqualTo(expectedValue);
  }

  @ParameterizedTest
  @CsvSource({
      "125, cb",
      "86203, wAx",
      "0, a",
      "1, b"
  })
  void testFromBase62(String expectedNumber, String encodedValue) {
    assertThat(service.decode(encodedValue))
        .isEqualTo(Double.valueOf(expectedNumber));
  }
}