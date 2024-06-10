package com.revolut.interview.stageone;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
public class AccountEntity {

  UUID uuid;
  BigDecimal balance;
}
