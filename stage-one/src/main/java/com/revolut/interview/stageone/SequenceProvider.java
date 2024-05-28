package com.revolut.interview.stageone;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SequenceProvider {

  final AtomicReference<BigInteger> bigIntegerAtomicReference = new AtomicReference<>(BigInteger.ZERO);

  public BigInteger getNext() {
    return bigIntegerAtomicReference.getAndUpdate(i -> i.add(BigInteger.ONE));
  }
}
