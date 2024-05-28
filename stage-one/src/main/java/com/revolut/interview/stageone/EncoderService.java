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
  static final String UPPERCASE_ALPHABET = StringUtils.upperCase("abcdefghijklmnopqrstuvwxyz");
  static final String DIGITS = "0123456789";
  static final String SYMBOLS = LOWERCASE_ALPHABET + UPPERCASE_ALPHABET + DIGITS;

  public String encode(BigInteger integer) {
    AtomicReference<BigInteger> atomicReference = new AtomicReference<>(integer);
    ArrayList<String> bits = new ArrayList<>();
    while (atomicReference.get().signum() > 0) {
      BigInteger bigInteger = atomicReference.getAndUpdate(i -> i.divide(BigInteger.valueOf(BASE)));
      BigInteger remainder = bigInteger.remainder(BigInteger.valueOf(BASE));
      bits.add(mapToChar(remainder));
    }
    return bits.stream().reduce((p, n) -> n + p).orElse(String.valueOf(SYMBOLS.charAt(0)));
  }

  private String mapToChar(BigInteger remainder) {
    return String.valueOf(SYMBOLS.charAt(remainder.intValue()));
  }

  static Integer mapToInt(Character character) {
    return SYMBOLS.indexOf(character);
  }

  public BigInteger decode(String input) {
    return Stream.ofAll(StringUtils.reverse(input).toCharArray())
        .zipWithIndex()
        .map(tuple -> mapToInt(tuple._1()) * Math.pow(BASE, tuple._2()))
        .map(i -> BigInteger.valueOf(i.longValue()))
        .reduce(BigInteger::add);
  }
}
