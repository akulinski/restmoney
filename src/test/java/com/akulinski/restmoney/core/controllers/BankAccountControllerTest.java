package com.akulinski.restmoney.core.controllers;

import com.akulinski.restmoney.ResourceRegistry;
import com.akulinski.restmoney.config.GsonTransformer;
import com.akulinski.restmoney.config.MockConfig;
import com.akulinski.restmoney.core.domain.BankAccount;
import com.akulinski.restmoney.core.domain.repository.BankAccountRepository;
import com.akulinski.restmoney.core.domain.repository.BankAccountRepositoryImpl;
import com.akulinski.restmoney.core.services.BankAccountService;
import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpClientException;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.sparkjava.test.SparkServer;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.http.HttpStatus;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.ClassRule;
import org.junit.Test;
import spark.servlet.SparkApplication;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class BankAccountControllerTest {
    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    private final static BankAccountRepository bankAccountRepository = new BankAccountRepositoryImpl(sessionFactory);
    private final static BankAccountService bankAccountService = new BankAccountService(bankAccountRepository);
    private final static MockConfig mockConfig = new MockConfig(bankAccountRepository);
    private final static GsonTransformer gsonTransformer = new GsonTransformer(gson);
    private final static BankAccountController bankAccountController = new BankAccountController(gson, bankAccountService);
    private final static ResourceRegistry resourceRegistry = new ResourceRegistry(gsonTransformer, bankAccountController, mockConfig);
    public static final String HTTP_LOCALHOST_8080_API_V_1 = "http://localhost:8080/api/v1";
    private final Faker faker = new Faker();

    @ClassRule
    public static SparkServer testServer = new SparkServer<>(BankAccountControllerTest.WebAppTestSparkApp.class, 8080);

    @Test
    public void findAllAccounts() throws HttpClientException {
        var accountNumber = new AtomicReference<>("9");

        Stream.generate(() -> {
            BankAccount account = new BankAccount();
            account.setAccountNumber(accountNumber.get());
            account.setBalance(0F);

            var newNumber = accountNumber.get() + "9";
            accountNumber.set(newNumber);

            return account;
        }).limit(10).forEach(bankAccountRepository::save);

        GetMethod getMethod = new GetMethod(HTTP_LOCALHOST_8080_API_V_1 + "/get-all-accounts", false);
        HttpResponse httpResponse = testServer.execute(getMethod);
        final var list = (List<BankAccount>) gson.fromJson(new String(httpResponse.body()), List.class);

        assertEquals(httpResponse.code(), HttpStatus.OK_200);
        assertEquals((Long) Integer.toUnsignedLong(list.size()), bankAccountRepository.count());
    }

    @Test
    public void create() throws HttpClientException {
        BankAccount bankAccount = new BankAccount();
        final var digits = faker.number().digits(255);
        bankAccount.setAccountNumber(digits);
        bankAccount.setBalance(0F);

        PostMethod postMethod = new PostMethod(HTTP_LOCALHOST_8080_API_V_1 + "/create-account", gson.toJson(bankAccount), false);

        HttpResponse httpResponse = testServer.execute(postMethod);

        final var account = (BankAccount) gson.fromJson(new String(httpResponse.body()), BankAccount.class);

        assertEquals(account.getAccountNumber(), digits);
    }

    @Test
    public void findById() throws HttpClientException {
        BankAccount bankAccount = new BankAccount();
        final var digits = faker.number().digits(255);
        bankAccount.setAccountNumber(digits);
        bankAccount.setBalance(0F);

        final var save = bankAccountRepository.save(bankAccount);

        GetMethod getMethod = new GetMethod(HTTP_LOCALHOST_8080_API_V_1 + "/find-by-id/" + save, false);
        HttpResponse httpResponse = testServer.execute(getMethod);
        final var accountFromServer = gson.fromJson(new String(httpResponse.body()), BankAccount.class);

        assertEquals(digits, accountFromServer.getAccountNumber());
    }

    @Test
    public void update() {
    }

    @Test
    public void transferMoney() {
    }


    public static class WebAppTestSparkApp implements SparkApplication {

        @Override
        public void init() {
            resourceRegistry.registerRoutes();
        }
    }
}
