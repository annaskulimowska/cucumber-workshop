package com.integration.bankingapp.controller;

import com.integration.bankingapp.model.Account;
import com.integration.bankingapp.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("accounts")
public class AccountController {

    private final AccountService service;

    AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping(produces = "application/json")
    public List<Account> getAccounts(@RequestParam("userId") String userId) {
        return service.getAccounts(userId);
    }

    @GetMapping(path = "/{accountNumber}", produces = "application/json")
    public ResponseEntity<Account> getAccountDetails(@PathVariable String accountNumber) {
        Optional<Account> maybeAccount = service.getAccount(accountNumber);
        if(maybeAccount.isPresent()){
            Account account = maybeAccount.get();
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.badRequest().build();
    }
}
