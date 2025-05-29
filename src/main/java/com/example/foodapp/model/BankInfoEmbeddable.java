package com.example.foodapp.model;

import jakarta.persistence.Embeddable;

/**
 * Mirrors DTO.BankInfo for JPA embedding.
 */
@Embeddable
public class BankInfoEmbeddable {
    private String bankName;
    private String accountNumber;

    public BankInfoEmbeddable() {}

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
