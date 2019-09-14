package com.akulinski.restmoney.core.domain.repository;

import com.akulinski.restmoney.core.domain.BankAccount;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BankAccountRepositoryImplTest {

    private SessionFactory sessionFactory = new Configuration().configure()
            .buildSessionFactory();

    private BankAccountRepository bankAccountRepository;

    @Before
    public void setUp() throws Exception {
        bankAccountRepository = new BankAccountRepositoryImpl(sessionFactory);
    }

    @Test
    public void save() {
        final Long countCurrent = bankAccountRepository.count();

        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("12345");
        bankAccount.setBalance(0F);

        bankAccountRepository.save(bankAccount);

        assertEquals(Long.valueOf(countCurrent + 1), bankAccountRepository.count());
    }

    @Test
    public void findByAccountNumber() {

        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("123");
        bankAccount.setBalance(0F);

        bankAccountRepository.save(bankAccount);

        final var byAccountNumber = bankAccountRepository.findByAccountNumber("123");

        assertTrue(byAccountNumber.isPresent());

    }

    @Test
    public void checkIfEmpty() {
        final var byAccountNumber = bankAccountRepository.findByAccountNumber("1");
        assertTrue(byAccountNumber.isEmpty());
    }


    @Test
    public void findAll() {

        final var currentCount = bankAccountRepository.count();

        var accountNumber = new AtomicReference<>("1");

        Stream.generate(() -> {
            BankAccount account = new BankAccount();
            account.setAccountNumber(accountNumber.get());
            account.setBalance(0F);

            var newNumber = accountNumber.get() + "1";
            accountNumber.set(newNumber);

            return account;
        }).limit(10).forEach(bankAccountRepository::save);

        final var sizeOfAll = bankAccountRepository.findAll().size();

        assertEquals(currentCount + 10, sizeOfAll);
    }
}
