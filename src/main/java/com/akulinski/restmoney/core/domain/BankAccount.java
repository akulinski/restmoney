package com.akulinski.restmoney.core.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "bank_account")
@Data
public class BankAccount {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "account_number", unique = true)
    private String accountNumber;

    @Column
    private Float balance;

}
