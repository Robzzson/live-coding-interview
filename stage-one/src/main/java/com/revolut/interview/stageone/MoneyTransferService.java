package com.revolut.interview.stageone;

import com.google.common.base.Preconditions;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MoneyTransferService {

  @Inject
  UserTransaction userTransaction;

  final AccountDao accountDao;
  final TransferDao transferDao;

  @Transactional(Transactional.TxType.REQUIRED)
  public TransferEntity transfer(AccountEntity from, AccountEntity to, BigDecimal amount) {

    Optional<AccountEntity> fromAccountOptional = accountDao.fetchWithLock(from.getUuid());
    Optional<AccountEntity> toAccountOptional = accountDao.fetchWithLock(to.getUuid());
    Preconditions.checkArgument(fromAccountOptional.isPresent());
    Preconditions.checkArgument(toAccountOptional.isPresent());

    AccountEntity fromAccount = fromAccountOptional.get();
    AccountEntity toAccount = toAccountOptional.get();

    fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
    toAccount.setBalance(toAccount.getBalance().add(amount));

    AccountEntity updated = accountDao.update(fromAccount);
    if (updated.getBalance().signum() == -1) {
      throw new RuntimeException("Negative balance in account [%s]".formatted(fromAccount.getUuid()));
    }
    accountDao.update(toAccount);

    TransferEntity transferEntity = new TransferEntity(UUID.randomUUID(), from.getUuid(), to.getUuid(), amount);
    transferDao.create(transferEntity);

    return transferEntity;
  }
}
