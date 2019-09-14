package com.akulinski.restmoney.core.controllers;


import com.akulinski.restmoney.core.domain.BankAccount;
import com.akulinski.restmoney.core.services.BankAccountService;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.List;

public class BankAccountController {

    private Gson gson;

    private BankAccountService bankAccountService;

    @Inject
    public BankAccountController(Gson gson, BankAccountService bankAccountService) {
        this.gson = gson;
        this.bankAccountService = bankAccountService;
    }

    public List<BankAccount> findAllAccounts(Request request, Response response) {
        return bankAccountService.getAllAccounts();
    }

    public void transferMoney(Request request, Response response) {

    }
}
