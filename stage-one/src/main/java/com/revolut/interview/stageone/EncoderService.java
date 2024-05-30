package com.revolut.interview.stageone;

import io.vavr.collection.Stream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class EncoderService {

  static final int BASE = 62;
  static final String LOWERCASE_ALPHABET = "abcdefghijklmnopqrstuvwxyz";
  static final String UPPERCASE_ALPHABET = StringUtils.upperCase(LOWERCASE_ALPHABET);
  static final String DIGITS = "0123456789";
  static final String SYMBOLS = LOWERCASE_ALPHABET + UPPERCASE_ALPHABET + DIGITS;

  public String encode(BigInteger integer) {
    AtomicReference<BigInteger> atomicReference = new AtomicReference<>(integer);
    ArrayList<Integer> indexes = new ArrayList<>();
    while (atomicReference.get().signum() > 0) {
      BigInteger bigInteger = atomicReference.getAndUpdate(i -> i.divide(BigInteger.valueOf(BASE)));
      Integer remainder = bigInteger.mod(BigInteger.valueOf(BASE)).intValueExact();
      indexes.add(remainder);
    }
    return indexes.stream()
        .map(EncoderService::mapToChar)
        .map(Object::toString)
        .reduce((p, n) -> n + p)
        .orElse(String.valueOf(SYMBOLS.charAt(0)));
  }

  public BigInteger decode(String input) {
    return Stream.ofAll(input.toCharArray())
        .reverse()
        .map(EncoderService::mapToInt)
        .zipWithIndex()
        .map(tuple -> BigInteger.valueOf(tuple._1()).multiply(BigInteger.valueOf(BASE).pow(tuple._2())))
        .reduce(BigInteger::add);
  }

  static Character mapToChar(Integer remainder) {
    return SYMBOLS.charAt(remainder);
  }

  static Integer mapToInt(Character character) {
    return SYMBOLS.indexOf(character);
  }
}
