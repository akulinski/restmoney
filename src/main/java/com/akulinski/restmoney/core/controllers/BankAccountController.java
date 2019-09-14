package com.akulinski.restmoney.core.controllers;


import com.akulinski.restmoney.core.domain.BankAccount;
import com.akulinski.restmoney.core.services.BankAccountService;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.List;

public class BankAccountController {


    private BankAccountService bankAccountService;

    @Inject
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    public List<BankAccount> findAllAccounts(Request request, Response response) {
        return bankAccountService.getAllAccounts();
    }
}
