package com.integration.bankingapp.service;

import java.math.BigDecimal;

public record CoreBankingAccount(String number, BigDecimal balance, String currency) {
}