package com.akulinski.restmoney.core.domain.repository;

import com.akulinski.restmoney.core.domain.BankAccount;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository {

    Long save(BankAccount bankAccount);

    Long count();

    Optional<BankAccount> findByAccountNumber(String bankAccount);

    Optional<BankAccount> findById(Long id);

    List findAll();

    Boolean transferMoney(String fromAccount, String toAccount, Float amount) throws IllegalArgumentException;

    void updateAndFlush(BankAccount bankAccount);

}
