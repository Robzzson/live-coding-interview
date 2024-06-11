package com.revolut.interview.stageone;

import lombok.Data;
import org.apache.commons.lang3.NotImplementedException;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AccountEntity implements Comparable<AccountEntity> {

  UUID id;
  BigDecimal balance;
  Integer version;

  @Override
  public int compareTo(AccountEntity o) {
    // compare alphabetically by string representation of 'id'
    throw new NotImplementedException();
  }

  // SELECT * FROM accounts WHERE id = ?
  // UPDATE ...() WHERE version = ?
}
