package com.revolut.interview.stageone;

import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;

public class UrlRepository {

  final ConcurrentHashMap<BigInteger, String> map = new ConcurrentHashMap<>();

  public void save(StringRecord record) {
    map.put(record.id, record.value);
  }

  public String getById(BigInteger s) {
    return map.get(s);
  }
}
