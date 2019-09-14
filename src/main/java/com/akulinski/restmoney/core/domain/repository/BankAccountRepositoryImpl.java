package com.akulinski.restmoney.core.domain.repository;

import com.akulinski.restmoney.core.domain.BankAccount;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class BankAccountRepositoryImpl implements BankAccountRepository {

    private SessionFactory sessionFactory;

    @Inject
    public BankAccountRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long save(BankAccount bankAccount) {
        final var session = sessionFactory.openSession();
        session.beginTransaction();

        final var save = session.save(bankAccount);
        session.flush();
        session.close();

        return (Long) save;
    }

    @Override
    public void update(BankAccount bankAccount) {

        final var session = sessionFactory.openSession();
        final var transaction = session.beginTransaction();
        try {
            session.update(bankAccount);
            session.flush();
        } catch (RuntimeException r) {
            transaction.rollback();
        } finally {
            session.close();
        }

    }

    @Override
    public Long count() {
        final var session = sessionFactory.openSession();
        final var query = session.createQuery("SELECT count(b) FROM BankAccount b");
        final var singleResult = query.getSingleResult();
        return (Long) singleResult;
    }

    @Override
    public Optional<BankAccount> findByAccountNumber(String accountNumber) {

        try (var session = sessionFactory.openSession()) {
            final var query = session.createQuery("SELECT b FROM BankAccount b WHERE b.accountNumber = :accountNumber");
            query.setParameter("accountNumber", accountNumber);
            final var singleResult = (BankAccount) query.getSingleResult();
            return Optional.of(singleResult);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List findAll() {

        final var session = sessionFactory.openSession();
        final var bankAccounts = session.createQuery("SELECT b FROM BankAccount b");

        return bankAccounts.getResultList();
    }

    @Override
    public Boolean transferMoney(String fromAccount, String toAccount, Float amount) {

        if (amount <= 0F) {
            throw new IllegalArgumentException(String.format("Non positive values not allowed. Got: %s", amount));
        }

        final var session = sessionFactory.openSession();
        final var transaction = session.beginTransaction();

        try {
            final var byAccountNumberFrom = findByAccountNumber(fromAccount).orElseThrow(() -> new IllegalArgumentException(String.format("No account with number: %s", fromAccount)));
            byAccountNumberFrom.setBalance(byAccountNumberFrom.getBalance() - amount);
            update(byAccountNumberFrom);

            final var byAccountNumberTo = findByAccountNumber(toAccount).orElseThrow(() -> new IllegalArgumentException(String.format("No account with number: %s", toAccount)));
            byAccountNumberTo.setBalance(byAccountNumberTo.getBalance() + amount);
            update(byAccountNumberTo);
            transaction.commit();

            return Boolean.TRUE;
        } catch (RuntimeException ex) {
            transaction.rollback();
        } finally {
            session.close();
        }

        return Boolean.FALSE;
    }


}
