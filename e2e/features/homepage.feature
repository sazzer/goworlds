Feature: Loading the home page

  Scenario: The home page renders correctly
    When I load the home page
    Then the page header reads "Application Name"
