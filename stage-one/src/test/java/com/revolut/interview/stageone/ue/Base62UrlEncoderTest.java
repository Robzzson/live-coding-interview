package com.revolut.interview.stageone.ue;

import static org.assertj.core.api.Assertions.assertThat;

import io.vavr.Tuple2;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class Base62UrlEncoderTest {

  private Base62UrlEncoder encoder;

  @BeforeEach
  void setUp() {
    this.encoder = new Base62UrlEncoder();
  }

  @ParameterizedTest
  @MethodSource("encodeTestCases")
  void encode(BigInteger bi, String expectedValue) {
    assertThat(encoder.encode(bi))
        .isEqualTo(expectedValue);
  }

  @ParameterizedTest
  @MethodSource("decodeTestCases")
  void decode(String id, BigInteger expectedBigInteger) {
    assertThat(encoder.decode(id))
        .isEqualTo(expectedBigInteger);
  }

  static Stream<Arguments> encodeTestCases() {
    return testTuples.stream().map(t -> Arguments.of(BigInteger.valueOf(t._2), t._1));
  }

  static Stream<Arguments> decodeTestCases() {
    return testTuples.stream().map(t -> Arguments.of(t._1, BigInteger.valueOf(t._2)));
  }

  static List<Tuple2<String, Integer>> testTuples = List.of(
      new Tuple2<>("a", 0),
      new Tuple2<>("b", 1),
      new Tuple2<>("A", 26),
      new Tuple2<>("B", 27),
      new Tuple2<>("9", 61),
      new Tuple2<>("zA2", 97766)
  );
}