package com.revolut.interview.stageone;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AccountEntity {

  UUID id;
  BigDecimal balance;
  Integer version;

  // SELECT * FROM accounts WHERE id = ?
  // UPDATE ...() WHERE version = ?
}
