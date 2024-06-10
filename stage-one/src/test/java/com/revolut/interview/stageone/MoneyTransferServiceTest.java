package com.revolut.interview.stageone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MoneyTransferServiceTest {

  private MoneyTransferService moneyTransferService;
  private AccountDao accountDao;

  @BeforeEach
  void setUp() {
    this.accountDao = new AccountDao();
    TransferDao transferDao = new TransferDao();
    this.moneyTransferService = new MoneyTransferService(accountDao, transferDao);
  }

  @Test
  void transferError_selfTransfer() {
    AccountEntity accountEntity = new AccountEntity(UUID.randomUUID(), BigDecimal.ZERO);
    accountDao.create(accountEntity);

    assertThatThrownBy(() -> moneyTransferService.transfer(accountEntity, accountEntity, BigDecimal.TEN))
        .isInstanceOfSatisfying(TransferException.class, e -> {
          assertThat(e).hasMessageContaining("transfer to self");
        });
  }

  @Test
  void transferError_insufficientFunds() {
    AccountEntity from = new AccountEntity(UUID.randomUUID(), BigDecimal.ZERO);
    AccountEntity to = new AccountEntity(UUID.randomUUID(), BigDecimal.ZERO);
    accountDao.create(from);
    accountDao.create(to);

    assertThatThrownBy(() -> moneyTransferService.transfer(from, to, BigDecimal.TEN))
        .isInstanceOfSatisfying(TransferException.class, e -> {
          assertThat(e).hasMessageContaining("Insufficient funds");
        });
  }

  @ParameterizedTest
  @ValueSource(strings = {"-1", "0"})
  void transferError_invalidTransferValue(String s) {
    BigDecimal amount = BigDecimal.valueOf(Long.parseLong(s));

    AccountEntity from = new AccountEntity(UUID.randomUUID(), BigDecimal.TEN);
    AccountEntity to = new AccountEntity(UUID.randomUUID(), BigDecimal.ZERO);
    accountDao.create(from);
    accountDao.create(to);

    assertThatThrownBy(() -> moneyTransferService.transfer(from, to, amount))
        .isInstanceOfSatisfying(TransferException.class, e -> {
          assertThat(e).hasMessageContaining("invalid value");
        });
  }
}