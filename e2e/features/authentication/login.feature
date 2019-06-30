Feature: Authentication: Logging in

  Background:
    Given I load the home page
    And a user exists with details:
      | Email Address | graham@grahamcox.co.uk |
      | Password      | superSecretPassword    |
    And I try to authenticate with an email address of "graham@grahamcox.co.uk"

  Scenario: An error is displayed if no password is entered
    When I try to log in with a password of ""
    Then I get errors logging in:
      | Password | Please enter a password |

  Scenario: An error is displayed if the wrong password is entered
    When I try to log in with a password of "wrongPassword"
    Then I get an error logging in of "Invalid password or account blocked"
