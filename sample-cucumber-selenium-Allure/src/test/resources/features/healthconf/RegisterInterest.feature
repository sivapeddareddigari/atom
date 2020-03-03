#------------------------------------------------------------------------------------------
#This feature uses standard approach of java coded cucumber steps + page objects.
#------------------------------------------------------------------------------------------

Feature: Delegate Registration
  As an interested conference delegate
  I want to register my interest in the conference
  So that I can get early notification of ticket availability

  @registerinterest
  Scenario: Register for Event (test 1)
    Given the application "HealthConference"
    When a trainee registers for an event
      | email        | bill.brown@email.com |
      | telephone    |          07771234567 |
      | street       | Fenchurch Street     |
      | city         | London               |
      | county       | Greater London       |
      | postcode     | EC3M 3BD             |
      | title        | Automation Tester    |
      | contactEmail | yes                  |
      | contactSMS   | no                   |
      | contactPost  | yes                  |
      | user         | bill brown           |
      | housenumber  |                   10 |
    Then a confirmation message is displayed "Thank you bill brown for registering for the conference"
