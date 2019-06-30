Feature: Authentication: Registering a new user

  Background:
    Given I load the home page
    When I try to authenticate with an email address of "graham@grahamcox.co.uk"

  Scenario Outline: Appropriate errors are displayed when the form is not filled out correctly: <Comment>
    When I try to register a user with details:
      | Name              | <Name>              |
      | Password          | <Password>          |
      | Re-enter Password | <Re-enter Password> |
    Then I get errors registering a user:
      | Name              | <Name Error>              |
      | Password          | <Password Error>          |
      | Re-enter Password | <Re-enter Password Error> |

    Examples:
      | Name   | Password | Re-enter Password | Name Error               | Password Error          | Re-enter Password Error      | Comment                         |
      |        |          |                   | Please enter a user name | Please enter a password |                              | No fields populated             |
      |        | password | password          | Please enter a user name |                         |                              | Name not populated              |
      | Graham |          | password          |                          | Please enter a password | Passwords do not match       | Password not populated          |
      | Graham | password |                   |                          |                         | Please re-enter the password | Re-enter Password not populated |
      | Graham | password | different         |                          |                         | Passwords do not match       | Passwords differ                |

  Scenario: An error occurs when registering a duplicate email address
    Given a user exists with details:
      | Email Address | graham@grahamcox.co.uk |
    When I try to register a user with details:
      | Name              | Graham   |
      | Password          | password |
      | Re-enter Password | password |
    Then I get an error registering a user of "Email Address is already registered"
