package com.integration.bankingapp.accounts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integration.bankingapp.service.CoreBankingAccount;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.integration.bankingapp.CucumberIntegrationTest.WIRE_MOCK_SERVER;

class WireMockUtils {

    static void createStubForAccountList(String userId, List<CoreBankingAccount> accounts, ObjectMapper objectMapper) throws JsonProcessingException {
        WIRE_MOCK_SERVER.stubFor(get(urlEqualTo("/core-banking-account?userId=" + userId))
                .willReturn(ok()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(accounts))
                ));
    }

    static void createStubForAccountDetails(String accountNumber, CoreBankingAccount coreBankingAccount, ObjectMapper objectMapper) throws JsonProcessingException {
        WIRE_MOCK_SERVER.stubFor(get(urlEqualTo("/core-banking-account/" + accountNumber)).atPriority(1)
                .willReturn(ok()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(coreBankingAccount))
                ));
    }

    static void createStubForInvalidAccountDetails() {
        WIRE_MOCK_SERVER.stubFor(get(urlPathMatching("/core-banking-account/.*")).atPriority(2)
                .willReturn(badRequest()
                        .withHeader("Content-Type", "application/json")
                        .withBody("Account not found")
                ));
    }
}