package com.akulinski.restmoney.core.services;

import com.akulinski.restmoney.core.domain.BankAccount;
import com.akulinski.restmoney.core.domain.repository.BankAccountRepository;
import com.akulinski.restmoney.core.domain.repository.BankAccountRepositoryImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class BankAccountServiceTest {

    private SessionFactory sessionFactory = new Configuration().configure()
            .buildSessionFactory();

    private BankAccountRepository bankAccountRepository;

    private BankAccountService bankAccountService;

    @Before
    public void setUp() throws Exception {
        bankAccountRepository = new BankAccountRepositoryImpl(sessionFactory);
        bankAccountService = new BankAccountService(bankAccountRepository);
    }


    @Test
    public void getAllAccounts() {
        final var currentCount = bankAccountRepository.count();

        var accountNumber = new AtomicReference<>("2");

        Stream.generate(() -> {
            BankAccount account = new BankAccount();
            account.setAccountNumber(accountNumber.get());
            account.setBalance(0F);

            var newNumber = accountNumber.get() + "2";
            accountNumber.set(newNumber);

            return account;
        }).limit(10).forEach(bankAccountRepository::save);

        final var sizeOfAll = bankAccountRepository.findAll().size();

        assertEquals(currentCount + 10, sizeOfAll);
    }

    @Test
    public void transferMoney() {
        BankAccount account = new BankAccount();
        account.setAccountNumber("123456789");
        account.setBalance(100F);

        bankAccountRepository.save(account);

        BankAccount account2 = new BankAccount();
        account2.setAccountNumber("12345678");
        account2.setBalance(0F);

        bankAccountRepository.save(account2);

        final var resultTransfer = bankAccountService.transferMoney(account.getAccountNumber(), account2.getAccountNumber(), 50F);

        assertTrue(resultTransfer);
    }

    @Test
    public void transferMoneyToNonExistentAccount() {
        BankAccount account = new BankAccount();
        account.setAccountNumber("123456789");
        account.setBalance(100F);

        bankAccountRepository.save(account);

        BankAccount account2 = new BankAccount();
        account2.setAccountNumber("jsdaof83802");
        account2.setBalance(0F);


        final var resultTransfer = bankAccountService.transferMoney(account.getAccountNumber(), account2.getAccountNumber(), 50F);

        assertFalse(resultTransfer);
    }

    @Test
    public void transferZeroMoney() {
        BankAccount account = new BankAccount();
        account.setAccountNumber("123456789");
        account.setBalance(100F);

        bankAccountRepository.save(account);

        BankAccount account2 = new BankAccount();
        account2.setAccountNumber("jsdaof83802sadsads21321");
        account2.setBalance(0F);

        bankAccountRepository.save(account2);

        final var result = bankAccountService.transferMoney(account.getAccountNumber(), account2.getAccountNumber(), 0F);

        assertFalse(result);
    }

}
