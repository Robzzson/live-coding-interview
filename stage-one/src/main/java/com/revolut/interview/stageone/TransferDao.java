package com.revolut.interview.stageone;

import jakarta.transaction.Transactional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TransferDao {

  private final ConcurrentHashMap<UUID, TransferEntity> map;

  public TransferDao() {
    this.map = new ConcurrentHashMap<>();
  }

  @Transactional
  public TransferEntity create(TransferEntity transferEntity) {
    map.put(transferEntity.getId(), transferEntity);
    return transferEntity;
  }
}
