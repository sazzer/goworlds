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

  Scenario Outline: Appropriate errors are displayed when the form is not filled out correctly: <Comment>
    Given I open the User Profile page
    When I update the User Profile form to:
      | Email Address | <Email> |
      | Name          | <Name>  |
    Then The User Profile Form has errors:
      | Email Address | <Email Error> |
      | Name          | <Name Error>  |
    And I am logged in as "Test User"
#    And I open the User Profile page
#    And The User Profile Form has details:
#      | Email Address | graham@grahamcox.co.uk |
#      | Name          | Test User              |

    Examples:
      | Name   | Email                  | Name Error               | Email Error                        | Comment                |
      |        |                        | Please enter a user name | Please enter an Email Address      | No fields populated    |
      |        | graham@grahamcox.co.uk | Please enter a user name |                                    | Name not populated     |
      | Graham |                        |                          | Please enter an Email Address      | Email not populated    |
      | Graham | invalid                |                          | Please enter a valid Email Address | Email not valid        |
