package com.revolut.interview.stageone;

import jakarta.transaction.Transactional;
import org.apache.commons.lang3.NotImplementedException;
import java.util.Optional;
import java.util.UUID;

public class AccountDao {

  @Transactional(Transactional.TxType.MANDATORY)
  // SELECT * FROM account FOR UPDATE;
  Optional<AccountEntity> fetchWithLock(UUID accountId) {
    throw new NotImplementedException();
  }

  public AccountEntity update(AccountEntity toAccount) {
    throw new NotImplementedException();
  }
}
