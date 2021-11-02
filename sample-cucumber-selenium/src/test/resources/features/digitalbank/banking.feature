Feature: Deposit/ Withdraw amount from account

  @digitalbanking
  Scenario Outline: Deposit/ Withdraw amount to/from account
    Given I login to the DigitalBanking application
    When I "<action>" "<amount>" into the checking account
    Then I must be able to see "<action>" of "<amount>" in the account
    Examples:
      | action            | amount     |
    |    deposit          | 1000       |
    |    deposit          | 2000       |
    |    withdraw         | 2000       |