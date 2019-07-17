Feature: User Profile: Profile Form

  Background:
    Given a user exists with details:
      | Email Address | graham@grahamcox.co.uk |
      | Password      | superSecretPassword    |
      | Name          | Test User              |
    And I load the home page
    And I try to authenticate with an email address of "graham@grahamcox.co.uk"
    And I try to log in with a password of "superSecretPassword"
    And I am logged in as "Test User"

  Scenario: The User Profile Form is populated correctly
    When I open the User Profile page
    Then The User Profile Form has details:
      | Email Address | graham@grahamcox.co.uk |
      | Name          | Test User              |

  Scenario: The User Profile Form can be saved correctly
    Given I open the User Profile page
    When I update the User Profile form to:
      | Email Address | new@example.com |
      | Name          | New Name        |
    Then The User Profile Form has details:
      | Email Address | new@example.com |
      | Name          | New Name        |
    And I am logged in as "New Name"

  Scenario: Changes to the User Profile form are persisted correctly
    Given I open the User Profile page
    And I update the User Profile form to:
      | Email Address | new@example.com |
      | Name          | New Name        |
    When I open the User Profile page
    Then The User Profile Form has details:
      | Email Address | new@example.com |
      | Name          | New Name        |
