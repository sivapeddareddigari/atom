#------------------------------------------------------------------------------------------
#This feature uses standard approach of java coded cucumber steps + page objects.
#------------------------------------------------------------------------------------------

Feature: Do Nothing Feature
  Simple scenario where message is custom message is printed in the HTML report

  @donothing
  Scenario: Do Nothing scenario
    Given do nothing
    And do something