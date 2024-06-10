package com.revolut.interview.stageone;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountEntity {

  UUID uuid;
  BigDecimal balance;
}
