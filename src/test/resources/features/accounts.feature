Feature: Accounts
  The user want to see his accounts

  Scenario: The user requests accounts list
    Given user with id "1234" has accounts in core banking system
      | accountNumber              | balance | currency |
      | 55109024027419789528223699 | 1000.00 | PLN      |
      | 42109024022482614283948657 | 50.00   | USD      |
    When user with id "1234" is viewing bank accounts
    Then user should see accounts
      | accountNumber                    | iban                               | balanceWithCurrency |
      | 55 1090 2402 7419 7895 2822 3699 | PL55 1090 2402 7419 7895 2822 3699 | 1000.00 PLN         |
      | 42 1090 2402 2482 6142 8394 8657 | PL42 1090 2402 2482 6142 8394 8657 | 50.00 USD           |