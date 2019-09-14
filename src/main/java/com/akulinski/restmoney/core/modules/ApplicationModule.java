package com.akulinski.restmoney.core.modules;

import com.akulinski.restmoney.config.GsonTransformer;
import com.akulinski.restmoney.config.MockConfig;
import com.akulinski.restmoney.core.controllers.BankAccountController;
import com.akulinski.restmoney.core.domain.repository.BankAccountRepository;
import com.akulinski.restmoney.core.services.BankAccountService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import spark.ResponseTransformer;


@Module
public class ApplicationModule {

    @Provides
    public Gson provideGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    @Provides
    public ResponseTransformer provideResponseTransformer(GsonTransformer gsonTransformer) {
        return gsonTransformer;
    }


    @Provides
    public MockConfig mockConfig(BankAccountRepository bankAccountRepository) {
        return new MockConfig(bankAccountRepository);
    }

    @Provides
    public BankAccountService bankAccountService(BankAccountRepository bankAccountRepository) {
        return new BankAccountService(bankAccountRepository);
    }

    @Provides
    public BankAccountController bankAccountController(BankAccountService bankAccountService, Gson gson) {
        return new BankAccountController(gson, bankAccountService);
    }
}
