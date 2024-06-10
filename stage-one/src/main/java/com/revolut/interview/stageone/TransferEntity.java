package com.revolut.interview.stageone;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransferEntity {

  final UUID id;
  final UUID from;
  final UUID to;
  final BigDecimal amount;
}
