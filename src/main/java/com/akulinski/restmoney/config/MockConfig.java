package com.akulinski.restmoney.config;

import com.akulinski.restmoney.core.domain.BankAccount;
import com.akulinski.restmoney.core.domain.repository.BankAccountRepository;
import com.github.javafaker.Faker;

import javax.inject.Inject;
import java.util.stream.Stream;

public class MockConfig {

    private BankAccountRepository bankAccountRepository;

    private Faker faker;

    @Inject
    public MockConfig(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
        faker = new Faker();
    }


    public void mockData() {
        if (bankAccountRepository.count() == 0) {
            Stream.generate(() -> {
                BankAccount bankAccount = new BankAccount();
                bankAccount.setAccountNumber(faker.number().digits(255));
                bankAccount.setBalance((float) faker.number().randomDouble(2, -100000, 100000));
                return bankAccount;
            }).limit(100).forEach(bankAccountRepository::save);
        }
    }
}
