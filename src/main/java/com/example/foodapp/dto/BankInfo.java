package com.example.foodapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for bank account information.
 */
public class BankInfo {
    @JsonProperty("bank_name")
    private String bankName;
    
    @JsonProperty("account_number")
    private String accountNumber;

    public BankInfo() {}

    public BankInfo(String bankName, String accountNumber) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
    }

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