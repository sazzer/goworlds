Feature: Authentication: Checking Email Addresses

  Background:
    Given I load the home page

  Scenario: An error is displayed if no email address is entered
    When I try to authenticate with an email address of ""
    Then I get an error entering the email address of "Please enter an Email Address"

  Scenario: An error is displayed if an invalid email address is entered
    When I try to authenticate with an email address of "invalid"
    Then I get an error entering the email address of "Please enter a valid Email Address"

  Scenario: The Register User screen is displayed if an unknown email address is entered
    When I try to authenticate with an email address of "graham@grahamcox.co.uk"
    Then the User Registration form is displayed

  Scenario: The Login screen is displayed if a known email address is entered
    Given a user exists with details:
      | Email Address | graham@grahamcox.co.uk |
    When I try to authenticate with an email address of "graham@grahamcox.co.uk"
    Then the Login form is displayed
