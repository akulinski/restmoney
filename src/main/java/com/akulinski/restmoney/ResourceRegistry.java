package com.akulinski.restmoney;

import com.akulinski.restmoney.config.MockConfig;
import com.akulinski.restmoney.core.controllers.BankAccountController;
import com.akulinski.restmoney.dto.ExceptionDTO;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import spark.ResponseTransformer;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.*;

@Singleton
@Slf4j
public class ResourceRegistry {

    private ResponseTransformer responseTransformer;

    private BankAccountController bankAccountController;

    private MockConfig mockConfig;

    private Gson gson;

    @Inject
    public ResourceRegistry(ResponseTransformer responseTransformer, BankAccountController bankAccountController, MockConfig mockConfig, Gson gson) {
        this.responseTransformer = responseTransformer;
        this.bankAccountController = bankAccountController;
        this.mockConfig = mockConfig;
        this.gson = gson;
    }

    public void registerRoutes() {
        if ("DEV".equals(System.getenv().get("RESTMONEY_ENVIROMENT"))) {
            mockConfig.mockData();
        }

        path("/api/v1", () -> {
            get("/get-all-accounts", "application/json", bankAccountController::findAllAccounts, responseTransformer);
            post("/transfer", "application/json", bankAccountController::transferMoney, responseTransformer);
            get("/find-by-id/:id", "application/json", bankAccountController::findById, responseTransformer);
            post("/create-account", "application/json", bankAccountController::create, responseTransformer);
            put("/update-account", "application/json", bankAccountController::update, responseTransformer);
        });

        exception(RuntimeException.class, (exception, request, response) -> {
            log.error(exception.getLocalizedMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
            ExceptionDTO exceptionDTO = new ExceptionDTO();
            exceptionDTO.setMessage(exception.getLocalizedMessage());
            exceptionDTO.setCause(exception.getCause());
            response.body(gson.toJson(exceptionDTO));
        });

        after(((request, response) -> response.type("application/json")));
    }
}
