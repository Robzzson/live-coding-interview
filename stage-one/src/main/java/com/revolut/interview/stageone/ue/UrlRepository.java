package com.revolut.interview.stageone.ue;

import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UrlRepository {

  final ConcurrentHashMap<BigInteger, String> map = new ConcurrentHashMap<>();

  public void save(UrlEntity urlEntity) {
    map.put(urlEntity.getId(), urlEntity.getValue());
  }

  public Optional<UrlEntity> getById(BigInteger id) {
    return Optional.ofNullable(map.get(id))
        .map(s -> new UrlEntity(id, s));
  }
}
