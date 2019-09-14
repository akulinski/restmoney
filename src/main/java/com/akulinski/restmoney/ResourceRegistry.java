package com.akulinski.restmoney;

import com.akulinski.restmoney.config.MockConfig;
import com.akulinski.restmoney.core.controllers.BankAccountController;
import spark.ResponseTransformer;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.get;

@Singleton
public class ResourceRegistry {

    private ResponseTransformer responseTransformer;

    private BankAccountController bankAccountController;

    private MockConfig mockConfig;

    @Inject
    ResourceRegistry(ResponseTransformer responseTransformer, BankAccountController bankAccountController, MockConfig mockConfig) {
        this.responseTransformer = responseTransformer;
        this.bankAccountController = bankAccountController;
        this.mockConfig = mockConfig;
    }

    public void registerRoutes() {
        mockConfig.mockData();

        get("/api/v1/get-all-accounts", "application/json", bankAccountController::findAllAccounts, responseTransformer);
    }
}
