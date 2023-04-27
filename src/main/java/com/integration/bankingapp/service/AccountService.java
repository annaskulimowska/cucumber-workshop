package com.integration.bankingapp.service;

import com.integration.bankingapp.model.Account;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class AccountService {

    private final RestTemplate restTemplate;
    @Value(("${webservice.url}"))
    private String webserviceUrl;

    public AccountService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Account> getAccounts(String userId) {
        ResponseEntity<List<CoreBankingAccount>> response = restTemplate.exchange(
                webserviceUrl + "/core-banking-account?userId=" + userId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody().stream()
                    .map(toAccount())
                    .toList();
        }
        return new ArrayList<>();
    }

    private Function<CoreBankingAccount, Account> toAccount() {
        return coreBankingAccount -> new Account(
                FormatUtils.formatAccountNumber(coreBankingAccount.number()),
                FormatUtils.formatIban("PL" + coreBankingAccount.number()),
                coreBankingAccount.balance() + " " + coreBankingAccount.currency());
    }

    public Optional<Account> getAccount(String accountNumber) {
        ResponseEntity<CoreBankingAccount> response;
        try {
            response = restTemplate.exchange(
                    webserviceUrl + "/core-banking-account/" + accountNumber,
                    HttpMethod.GET,
                    null,
                    CoreBankingAccount.class);
        } catch (HttpClientErrorException e) {
            response = new ResponseEntity<>(e.getStatusCode());
        }
        if (response.getStatusCode() == HttpStatus.OK) {
            CoreBankingAccount coreBankingAccount = response.getBody();
            return Optional.of(toAccount().apply(coreBankingAccount));
        }
        return Optional.empty();
    }
}