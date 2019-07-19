Feature: Authentication: Logging out

  Scenario: Logging out works correctly
    Given a user exists with details:
      | Email Address | graham@grahamcox.co.uk |
      | Password      | superSecretPassword    |
      | Name          | Test User              |
    And I load the home page
    And I try to authenticate with an email address of "graham@grahamcox.co.uk"
    And I try to log in with a password of "superSecretPassword"
    When I log out
    Then I am not logged in