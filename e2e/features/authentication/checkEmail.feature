Feature: Authentication: Checking Email Addresses

  Background:
    Given I load the home page

  Scenario: An error is displayed if no email address is entered
    When I try to authenticate with an email address of ""
    Then I get an error entering the email address of "Please enter an Email Address"

  Scenario: An error is displayed if an invalid email address is entered
    When I try to authenticate with an email address of "invalid"
    Then I get an error entering the email address of "Please enter a valid Email Address"

  Scenario: The Register User is displayed if an unknown email address is entered
    When I try to authenticate with an email address of "graham@grahamcox.co.uk"
    Then the User Registration form is displayed
