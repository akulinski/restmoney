package com.akulinski.restmoney;

import com.akulinski.restmoney.config.MockConfig;
import com.akulinski.restmoney.core.controllers.BankAccountController;
import spark.ResponseTransformer;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.*;

@Singleton
public class ResourceRegistry {

    private ResponseTransformer responseTransformer;

    private BankAccountController bankAccountController;

    private MockConfig mockConfig;

    @Inject
    public ResourceRegistry(ResponseTransformer responseTransformer, BankAccountController bankAccountController, MockConfig mockConfig) {
        this.responseTransformer = responseTransformer;
        this.bankAccountController = bankAccountController;
        this.mockConfig = mockConfig;
    }

    public void registerRoutes() {
        if ("DEV".equals(System.getenv().get("_RESTMONEY_ENVIROMENT"))) {
            mockConfig.mockData();
        }

        path("/api/v1", () -> {
            get("/get-all-accounts", "application/json", bankAccountController::findAllAccounts, responseTransformer);
            post("/transfer", "application/json", bankAccountController::transferMoney, responseTransformer);
            get("/find-by-id/:id", "application/json", bankAccountController::findById, responseTransformer);
            post("/create-account", "application/json", bankAccountController::create, responseTransformer);
            put("/update-account", "application/json", bankAccountController::update, responseTransformer);
        });

        after(((request, response) -> response.type("application/json")));
    }
}
