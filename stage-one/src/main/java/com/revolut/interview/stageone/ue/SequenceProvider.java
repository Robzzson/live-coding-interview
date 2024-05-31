package com.revolut.interview.stageone.ue;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SequenceProvider {

  final AtomicReference<BigInteger> atomicReference = new AtomicReference<>(BigInteger.ZERO);

  public BigInteger getNext() {
    return atomicReference.getAndUpdate(bigInteger -> bigInteger.add(BigInteger.ONE));
  }
}
