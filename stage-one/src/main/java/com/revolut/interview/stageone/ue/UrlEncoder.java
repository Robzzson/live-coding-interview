package com.revolut.interview.stageone.ue;

import java.math.BigInteger;

public interface UrlEncoder {

  String encode(BigInteger bigInteger);

  BigInteger decode(String identifier);
}
