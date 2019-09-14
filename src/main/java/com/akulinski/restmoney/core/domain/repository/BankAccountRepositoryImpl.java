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
    public Long count() {
        final var session = sessionFactory.openSession();
        final var query = session.createQuery("SELECT count(b) FROM BankAccount b");
        final var singleResult = query.getSingleResult();
        return (Long) singleResult;
    }

    @Override
    public Optional<BankAccount> findByAccountNumber(String accountNumber) {
        try {
            final var session = sessionFactory.openSession();
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
}
