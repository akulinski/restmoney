package com.akulinski.restmoney.core.services;

import com.akulinski.restmoney.core.domain.BankAccount;
import com.akulinski.restmoney.core.domain.repository.BankAccountRepository;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
public class BankAccountService {

    private BankAccountRepository bankAccountRepository;

    @Inject
    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public List<BankAccount> getAllAccounts() {
        return (List<BankAccount>) bankAccountRepository.findAll();
    }

    public BankAccount create(BankAccount bankAccount) {
        final var save = bankAccountRepository.save(bankAccount);
        return bankAccountRepository.findById(save).orElseThrow(() -> new IllegalStateException("No results after creating bank account"));
    }

    public void update(BankAccount bankAccount) {
        bankAccountRepository.updateAndFlush(bankAccount);
    }

    public Boolean transferMoney(String fromAccount, String toAccount, Float amount) {

        try {
            return bankAccountRepository.transferMoney(fromAccount, toAccount, amount);
        } catch (IllegalArgumentException ex) {
            log.error(ex.getLocalizedMessage());
        }

        return Boolean.FALSE;
    }


    public BankAccount findById(Long id) {
        return bankAccountRepository.findById(id).orElseThrow(() -> new NoSuchElementException(String.format("No results for id: %d", id)));
    }
}
