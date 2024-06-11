package com.revolut.interview.stageone;

import com.google.common.base.Preconditions;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BalanceTransferService {

  final AccountDao accountDao;

  final ReentrantLock lock = new ReentrantLock();

  @Transactional(Transactional.TxType.REQUIRED)
  void transfer(AccountEntity from, AccountEntity to, BigDecimal amount) {
    // from != to
    // amount > from.getBalance()
    // amount > 0

    Optional<AccountEntity> fromAccountOptional = accountDao.fetchWithLock(from.getId());
    Optional<AccountEntity> toAccountOptional = accountDao.fetchWithLock(to.getId());

    Preconditions.checkArgument(fromAccountOptional.isPresent() && toAccountOptional.isPresent());

    AccountEntity fromAccount = fromAccountOptional.get();
    AccountEntity toAccount = toAccountOptional.get();
//      Comparator.comparing()
    Integer version = fromAccount.getVersion();
    AtomicInteger atomicInteger = new AtomicInteger(version);

    if (fromAccount.compareTo(toAccount) > 0) {
//      synchronized (fromAccount) {
//        synchronized (toAccount) {
    } else {
//      synchronized (toAccount) {
//        synchronized (fromAccount) {
    }

    synchronized (fromAccount) {
      synchronized (toAccount) {
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        AccountEntity updatedAccount = accountDao.update(fromAccount);
        if (updatedAccount.getBalance().signum() < 0) {
          throw new RuntimeException();
        }

        AccountEntity accountEntity = accountDao.fetchWithLock(from.getId()).get();
//        atomicInteger.compareAndSet(accountEntity.version, )

        accountDao.update(toAccount);
      }
    }
  }
}
