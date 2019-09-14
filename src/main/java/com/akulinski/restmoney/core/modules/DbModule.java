package com.akulinski.restmoney.core.modules;

import com.akulinski.restmoney.core.domain.repository.BankAccountRepository;
import com.akulinski.restmoney.core.domain.repository.BankAccountRepositoryImpl;
import dagger.Module;
import dagger.Provides;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Module
public class DbModule {

    @Provides
    public SessionFactory sessionFactory() {
        return new Configuration().configure()
                .buildSessionFactory();
    }

    @Provides
    public BankAccountRepository bankAccountRepository(SessionFactory sessionFactory) {
        return new BankAccountRepositoryImpl(sessionFactory);
    }


}
