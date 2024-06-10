package com.revolut.interview.stageone;

import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AccountDao {

  private final ConcurrentHashMap<UUID, AccountEntity> map;

  public AccountDao() {
    this.map = new ConcurrentHashMap<>();
  }

  @Transactional(Transactional.TxType.MANDATORY)
  Optional<AccountEntity> fetchWithLock(final UUID id) {
    // SELECT * FROM accounts AS a WHERE a.id = ? FOR UPDATE;
    return Optional.ofNullable(map.get(id));
  }

  @Transactional
  public AccountEntity update(AccountEntity accountEntity) {
    this.map.put(accountEntity.getUuid(), accountEntity);
    return accountEntity;
  }
}
