package com.revolut.interview.stageone;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class EncoderServiceTest {

  private EncoderService encoder;

  @BeforeEach
  void setUp() {
    this.encoder = new EncoderService();
  }

  @ParameterizedTest
  @MethodSource("encodingTestCases")
  void testEncode(String input, String expectedValue) {
    assertThat(encoder.encode(new BigInteger(input)))
        .isEqualTo(expectedValue);
  }

  static Stream<Arguments> encodingTestCases() {
    return Stream.of(
        Arguments.of("0", "a"),
        Arguments.of("1", "b"),
        Arguments.of("26", "A"),
        Arguments.of("27", "B"),
        Arguments.of("61", "9"),
        Arguments.of("886580160", "8aaaa")
    );
  }

  @ParameterizedTest
  @MethodSource("decodingTestCases")
  void testDecode(String expectedValue, String input) {
    assertThat(encoder.decode(input))
        .isEqualTo(new BigInteger(expectedValue));
  }

  static Stream<Arguments> decodingTestCases() {
    return Stream.of(
        Arguments.of("0", "a"),
        Arguments.of("1", "b"),
        Arguments.of("26", "A"),
        Arguments.of("27", "B"),
        Arguments.of("61", "9"),
        Arguments.of("886580160", "8aaaa")
    );
  }
}