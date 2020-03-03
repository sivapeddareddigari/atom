#------------------------------------------------------------------------------------------
#This feature uses standard approach of java coded cucumber steps + page objects.
#------------------------------------------------------------------------------------------
@donothing
Feature: Do Nothing Feature
  Simple scenario where message is custom message is printed in the HTML report

  @issue=1234
  @tmsLink=TC01
  Scenario: Do Nothing scenario
    Given do nothing
    And do something


  Scenario:  a failing scenario
    Given do nothing
    And a failing step
