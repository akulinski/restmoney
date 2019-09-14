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

    @Test
    public void count() {
    }

    @Test
    public void transferMoneyBasicTest() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("123");
        bankAccount.setBalance(1000F);

        bankAccountRepository.save(bankAccount);

        BankAccount bankAccount2 = new BankAccount();
        bankAccount2.setAccountNumber("1234");
        bankAccount2.setBalance(0F);

        bankAccountRepository.save(bankAccount2);


        final var result = bankAccountRepository.transferMoney("123", "1234", 500F);
        assertTrue(result);
    }

    @Test
    public void transferMoney() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("123");
        bankAccount.setBalance(1000F);

        bankAccountRepository.save(bankAccount);

        BankAccount bankAccount2 = new BankAccount();
        bankAccount2.setAccountNumber("1234");
        bankAccount2.setBalance(0F);

        bankAccountRepository.save(bankAccount2);


        final var result = bankAccountRepository.transferMoney("123", "1234", 500F);

        final var byAccountNumber = bankAccountRepository.findByAccountNumber("123");
        final var byAccountNumber2 = bankAccountRepository.findByAccountNumber("1234");

        assertEquals(byAccountNumber.get().getBalance(), (Float) 500F);
        assertEquals(byAccountNumber2.get().getBalance(), (Float) 500F);

    }

    @Test
    public void transferMoneyToNonExistentAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("123");
        bankAccount.setBalance(1F);

        bankAccountRepository.save(bankAccount);


        final var result = bankAccountRepository.transferMoney("123", "jksahdfiadfiafiha", 1F);

        final var byAccountNumber = bankAccountRepository.findByAccountNumber("123");

        assertEquals(byAccountNumber.get().getBalance(), (Float) 1F);
    }

    @Test
    public void transferMoneyFromNonExistentAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("123");
        bankAccount.setBalance(1F);

        bankAccountRepository.save(bankAccount);


        final var result = bankAccountRepository.transferMoney("cvdfsafdadadfafdadsafsa", "123", 1F);

        final var byAccountNumber = bankAccountRepository.findByAccountNumber("123");

        assertEquals(byAccountNumber.get().getBalance(), (Float) 1F);
    }


    @Test
    public void transferMoneyWithNullAccountNumber() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("123");
        bankAccount.setBalance(1F);

        bankAccountRepository.save(bankAccount);


        final var result = bankAccountRepository.transferMoney(null, "123", 1F);

        final var byAccountNumber = bankAccountRepository.findByAccountNumber("123");

        assertEquals(byAccountNumber.get().getBalance(), (Float) 1F);
    }


    @Test(expected = IllegalArgumentException.class)
    public void transferMoneyWithBadArgument() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("123");
        bankAccount.setBalance(1000F);

        bankAccountRepository.save(bankAccount);

        BankAccount bankAccount2 = new BankAccount();
        bankAccount2.setAccountNumber("1234");
        bankAccount2.setBalance(0F);

        bankAccountRepository.save(bankAccount2);


        final var result = bankAccountRepository.transferMoney("123", "1234", null);

        final var byAccountNumber = bankAccountRepository.findByAccountNumber("123");
        final var byAccountNumber2 = bankAccountRepository.findByAccountNumber("1234");

        assertEquals((Float) 1000F, byAccountNumber.get().getBalance());
        assertEquals((Float) 0F, byAccountNumber2.get().getBalance());

    }

    @Test
    public void transferMoneyAndCheckNegative() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("123");
        bankAccount.setBalance(1000F);

        bankAccountRepository.save(bankAccount);

        BankAccount bankAccount2 = new BankAccount();
        bankAccount2.setAccountNumber("1234");
        bankAccount2.setBalance(0F);

        bankAccountRepository.save(bankAccount2);


        final var result = bankAccountRepository.transferMoney("123", "1234", 1001F);

        final var byAccountNumber = bankAccountRepository.findByAccountNumber("123");
        final var byAccountNumber2 = bankAccountRepository.findByAccountNumber("1234");

        assertEquals(Float.valueOf(-1F), byAccountNumber.get().getBalance());
        assertEquals((Float) 1001F, byAccountNumber2.get().getBalance());

    }


    @Test
    public void transferMoneyBigAmount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("123");
        bankAccount.setBalance(0F);

        bankAccountRepository.save(bankAccount);

        BankAccount bankAccount2 = new BankAccount();
        bankAccount2.setAccountNumber("1234");
        bankAccount2.setBalance(0F);

        bankAccountRepository.save(bankAccount2);


        final var result = bankAccountRepository.transferMoney("123", "1234", 100000000000000000000000000000000000000F);

        final var byAccountNumber = bankAccountRepository.findByAccountNumber("123");
        final var byAccountNumber2 = bankAccountRepository.findByAccountNumber("1234");

        assertEquals(Float.valueOf(-100000000000000000000000000000000000000F), byAccountNumber.get().getBalance());
        assertEquals((Float) 100000000000000000000000000000000000000F, byAccountNumber2.get().getBalance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void transferMoneyZero() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("123");
        bankAccount.setBalance(0F);

        bankAccountRepository.save(bankAccount);

        BankAccount bankAccount2 = new BankAccount();
        bankAccount2.setAccountNumber("1234");
        bankAccount2.setBalance(0F);

        bankAccountRepository.save(bankAccount2);
        bankAccountRepository.transferMoney("123", "1234", 0F);
    }

    @Test
    public void update() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("123");
        bankAccount.setBalance(1000F);

        bankAccountRepository.save(bankAccount);

        final var bankAccount1 = bankAccountRepository.findByAccountNumber("123").get();
        bankAccount1.setBalance(1F);
        bankAccountRepository.updateAndFlush(bankAccount1);

        assertEquals((Float) 1F, bankAccountRepository.findByAccountNumber("123").get().getBalance());
    }

    @Test
    public void updateWithNull() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountNumber("123");
        bankAccount.setBalance(1000F);

        bankAccountRepository.save(bankAccount);

        final BankAccount bankAccount1 = null;
        bankAccountRepository.updateAndFlush(bankAccount1);
        assertEquals((Float) 1000F, bankAccountRepository.findByAccountNumber("123").get().getBalance());
    }

}
