package com.akulinski.restmoney.core.services;

import com.akulinski.restmoney.core.domain.BankAccount;
import com.akulinski.restmoney.core.domain.repository.BankAccountRepository;

import javax.inject.Inject;
import java.util.List;

public class BankAccountService {

    private BankAccountRepository bankAccountRepository;

    @Inject
    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public List<BankAccount> getAllAccounts() {
        return (List<BankAccount>) bankAccountRepository.findAll();
    }

    public Boolean transferMoney(String fromAccount, String toAccount, Float amount) {
        return Boolean.FALSE;
    }
}
