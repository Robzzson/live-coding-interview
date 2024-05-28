package com.revolut.interview.stageone;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AtomicDouble;
import io.vavr.Tuple2;
import io.vavr.collection.Stream;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class Base62Service {

  static final int BASE = 62;
  static final String LOWERCASE_ALPHABET = "abcdefghijklmnopqrstuvwxyz";
  static final String UPPERCASE_ALPHABET = StringUtils.upperCase(LOWERCASE_ALPHABET);
  static final String DIGITS = "0123456789";
  static final String SYMBOLS = LOWERCASE_ALPHABET + UPPERCASE_ALPHABET + DIGITS;

  public String encode(Double d) {
    AtomicDouble atomicDouble = new AtomicDouble(d);
    ArrayList<String> bits = new ArrayList<>();
    while (atomicDouble.get() > 0) {
      double ad = atomicDouble.getAndUpdate(a -> Math.floor(a / 62));
      double remainder = ad % BASE;
      bits.add(mapToChar(remainder).toString());
    }
    return bits.stream().reduce((p, n) -> n + p).orElse("a");
  }

  public Double decode(String s) {
    return Stream.ofAll(StringUtils.reverse(s).toCharArray())
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
