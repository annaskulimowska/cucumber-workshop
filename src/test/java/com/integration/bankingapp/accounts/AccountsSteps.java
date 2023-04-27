package com.integration.bankingapp.accounts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integration.bankingapp.model.Account;
import com.integration.bankingapp.service.AccountService;
import com.integration.bankingapp.service.CoreBankingAccount;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.integration.bankingapp.CucumberIntegrationTest.HTTP_CLIENT;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountsSteps {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    int port;
    @Value(("${webservice.url}"))
    private String webserviceUrl;

    private CloseableHttpResponse lastResponse;

    @Given("user with id {string} has accounts in core banking system")
    public void user_with_id_has_accounts_in_core_banking_system(String userId, DataTable dataTable) throws JsonProcessingException {
        List<CoreBankingAccount> accounts = new ArrayList<>();
        List<Map<String, String>> rows = dataTable.asMaps();
        for (Map<String, String> row : rows) {
            String accountNumber = row.get("accountNumber");
            CoreBankingAccount coreBankingAccount = new CoreBankingAccount(accountNumber, new BigDecimal(row.get("balance")), row.get("currency"));
            accounts.add(coreBankingAccount);
            WireMockUtils.createStubForAccountDetails(accountNumber, coreBankingAccount, objectMapper);
        }
        WireMockUtils.createStubForAccountList(userId, accounts, objectMapper);
        WireMockUtils.createStubForInvalidAccountDetails();
    }

    @When("user with id {string} is viewing bank accounts")
    public void user_is_viewing_bank_accounts(String userId) throws IOException {
        executeRequest("/accounts?userId=" + userId);
    }

    private void executeRequest(String path) throws IOException {
        HttpGet request = new HttpGet("http://localhost:" + port + path);
        request.addHeader("accept", "application/json");
        lastResponse = HTTP_CLIENT.execute(request);
    }

    @Then("user should see accounts")
    public void user_should_see_accounts(DataTable dataTable) throws IOException {
        List<Account> expectedAccounts = new ArrayList<>();
        List<Map<String, String>> rows = dataTable.asMaps();
        for (Map<String, String> row : rows) {
            expectedAccounts.add(new Account(row.get("accountNumber"), row.get("iban"), row.get("balanceWithCurrency")));
        }

        InputStream content = lastResponse.getEntity().getContent();
        List<Account> responseAccounts = objectMapper.readValue(content, new TypeReference<>() {
        });

        assertEquals(expectedAccounts, responseAccounts);
    }
}