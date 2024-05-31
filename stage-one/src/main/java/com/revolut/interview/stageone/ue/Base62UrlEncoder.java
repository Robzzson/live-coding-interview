package com.revolut.interview.stageone.ue;

import io.vavr.collection.Stream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.lang3.StringUtils;

public class Base62UrlEncoder implements UrlEncoder {

  static final BigInteger BASE = BigInteger.valueOf(62);
  static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
  static final String UPPERCASE_LETTERS = StringUtils.upperCase(LOWERCASE_LETTERS);
  static final String DIGITS = "0123456789";
  public static final String SYMBOLS = LOWERCASE_LETTERS + UPPERCASE_LETTERS + DIGITS;

  @Override
  public String encode(BigInteger bigInteger) {
    AtomicReference<BigInteger> atomicReference = new AtomicReference<>(bigInteger);
    ArrayList<Integer> indexes = new ArrayList<>();
    while (atomicReference.get().signum() > 0) {
      BigInteger integer = atomicReference.getAndUpdate(bi -> bi.divide(BASE));
      int remainder = integer.mod(BASE).intValueExact();
      indexes.add(remainder);
    }
    return indexes.stream()
        .map(Base62UrlEncoder::mapToChar)
        .map(Object::toString)
        .reduce((p, n) -> n + p)
        .orElse(String.valueOf(SYMBOLS.charAt(0)));
  }

  @Override
  public BigInteger decode(String identifier) {
    return Stream.ofAll(identifier.toCharArray())
        .map(Base62UrlEncoder::mapToInt)
        .map(BigInteger::valueOf)
        .reverse()
        .zipWithIndex()
        .map(tuple -> tuple._1().multiply(BASE.pow(tuple._2())))
        .reduce(BigInteger::add);
  }

  private static Character mapToChar(Integer integer) {
    return SYMBOLS.charAt(integer);
  }

  private static Integer mapToInt(Character character) {
    return SYMBOLS.indexOf(character);
  }
}
