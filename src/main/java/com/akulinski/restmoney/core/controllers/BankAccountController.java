package com.akulinski.restmoney.core.controllers;


import com.akulinski.restmoney.core.domain.BankAccount;
import com.akulinski.restmoney.core.services.BankAccountService;
import com.akulinski.restmoney.dto.MoneyTransferDTO;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.List;

@Slf4j
public class BankAccountController {

    private Gson gson;

    private BankAccountService bankAccountService;

    @Inject
    public BankAccountController(Gson gson, BankAccountService bankAccountService) {
        this.gson = gson;
        this.bankAccountService = bankAccountService;
    }

    public List<BankAccount> findAllAccounts(Request request, Response response) {
        response.status(HttpStatus.OK_200);
        return bankAccountService.getAllAccounts();
    }

    public BankAccount create(Request request, Response response) {

        BankAccount bankAccount = gson.fromJson(request.body(), BankAccount.class);

        final var accountCreated = bankAccountService.create(bankAccount);
        response.status(HttpStatus.CREATED_201);
        return accountCreated;
    }


    public BankAccount findById(Request request, Response response) {
        try {
            final var idParsed = Long.parseLong(request.params(":id"));
            response.status(HttpStatus.OK_200);
            return bankAccountService.findById(idParsed);
        } catch (NumberFormatException ex) {
            log.error(ex.getLocalizedMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
            return new BankAccount();
        }
    }

    public BankAccount update(Request request, Response response) {
        BankAccount bankAccountFromReq = gson.fromJson(request.body(), BankAccount.class);
        bankAccountService.update(bankAccountFromReq);
        response.status(HttpStatus.OK_200);
        return bankAccountFromReq;
    }

    public Boolean transferMoney(Request request, Response response) {

        MoneyTransferDTO moneyTransferDTO = gson.fromJson(request.body(), MoneyTransferDTO.class);

        return bankAccountService.transferMoney(moneyTransferDTO.getFromBankAccount(), moneyTransferDTO.getToBankAccount(), moneyTransferDTO.getAmount());
    }
}
