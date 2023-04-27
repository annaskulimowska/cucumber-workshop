package com.integration.bankingapp.model;

public record Account(String number, String iban, String balanceWithCurrency) {
}