Feature: Calculator
  As a user
  I want to add numbers

  @calculator
  Scenario: Do Sum Correct
    Given the mobile application "Calculator"
    When the user does a sum correct

  @calculator
  Scenario: Do Sum Incorrect
    Given the mobile application "Calculator"
    When the user does a sum incorrect