package com.akulinski.restmoney.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MoneyTransferDTO implements Serializable {

    private String fromBankAccount;
    private String toBankAccount;
    private Float amount;
}
