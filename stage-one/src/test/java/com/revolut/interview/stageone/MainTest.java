package com.revolut.interview.stageone;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AtomicDouble;
import io.vavr.Tuple2;
import io.vavr.collection.Stream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MainTest {

  public static final int BASE = 62;
  public static final String LOWERCASE_ALPHABET = "abcdefghijklmnopqrstuvwxyz";
  public static final String UPPERCASE_ALPHABET = StringUtils.upperCase(LOWERCASE_ALPHABET);
  public static final String DIGITS = "0123456789";
  public static final String SYMBOLS = LOWERCASE_ALPHABET + UPPERCASE_ALPHABET + DIGITS;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void test() {
    assertThat(true).isTrue();
    // 0...61
    // aaaaaaaa ->
//    Math.pow()
//    String expected = "100101001000";
//    int number = 2376;
    String expected = "1011100101";
    int number = 741;
    assertThat(Integer.toBinaryString(number)).isEqualTo(expected);
    assertThat(MainTest.toBinary(number)).isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource({
      "125, cb",
      "86203, wAx",
      "0, a",
      "1, b"
  })
  void testToBase62(String number, String expectedValue) {
    assertThat(toBase62(Double.valueOf(number)))
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
    assertThat(fromBase62(encodedValue))
        .isEqualTo(Double.valueOf(expectedNumber));
  }

  static String toBinary(Integer integer) {
    AtomicInteger atomicInteger = new AtomicInteger(integer);
    ArrayList<String> bits = new ArrayList<>();
    while (atomicInteger.get() > 0) {
      int ai = atomicInteger.getAndUpdate(a -> a / 2);
      bits.add(String.valueOf(ai % 2));
    }
    return bits.stream().reduce((p, n) -> n + p).orElse("0");
  }

  static String toBase62(Double d) {
    AtomicDouble atomicDouble = new AtomicDouble(d);
    ArrayList<String> bits = new ArrayList<>();
    while (atomicDouble.get() > 0) {
      double ad = atomicDouble.getAndUpdate(a -> Math.floor(a / 62));
      double remainder = ad % BASE;
      bits.add(mapToChar(remainder).toString());
    }
    return bits.stream().reduce((p, n) -> n + p).orElse("a");
  }

  static Double fromBase62(String encoded) {
    return Stream.ofAll(StringUtils.reverse(encoded).toCharArray())
        .zipWithIndex()
        .map(tuple -> mapToInt(tuple._1()) * Math.pow(BASE, tuple._2()))
        .reduce(Double::sum);
  }

  static Character mapToChar(Double d) {
    Preconditions.checkArgument(d >= 0, "must not be negative");
    Preconditions.checkArgument(d < 62, "must be less than %s".formatted(BASE));
    return SYMBOLS.charAt(Math.toIntExact(d.longValue()));
  }

  static Integer mapToInt(Character character) {
    return Stream.ofAll((SYMBOLS).toCharArray())
        .zipWithIndex()
        .find(tuple -> tuple._1() == character)
        .map(Tuple2::_2)
        .getOrElseThrow(IllegalArgumentException::new);
  }
}