Feature: Delegate Registration
  As a shopper
  I want to find Walmart products

  @productSearch
  Scenario: Search Product
    Given the mobile application "Walmart"
    When the customer shops by department
    |Electronics -> Audio -> Headphones -> All Headphones|
    And chooses to purchase
    |Apple AirPods|
    Then the shopping cart will show the correct total price