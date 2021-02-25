#------------------------------------------------------------------------------------------
#This feature uses standard approach of java coded cucumber steps + page objects.
#------------------------------------------------------------------------------------------

Feature: Delegate Registration
  As an interested conference delegate
  I want to register my interest in the conference
  So that I can get early notification of ticket availability

  @TimofeyPirogovWixTest
  Scenario: Register for Event (test 1)
    Given the application "TimofeyPirogovWixTest"
    When User Filled Forms
      | email        | bill.brown@email.com |
      | user         | bill brown           |

