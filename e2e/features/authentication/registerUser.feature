Feature: Authentication: Registering a new user

  Background:
    Given I load the home page

  Scenario Outline: Appropriate errors are displayed when the form is not filled out correctly: <Comment>
    Given I try to authenticate with an email address of "graham@grahamcox.co.uk"
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
    Given I try to authenticate with an email address of "graham@grahamcox.co.uk"
    Given a user exists with details:
      | Email Address | graham@grahamcox.co.uk |
    When I try to register a user with details:
      | Name              | Graham   |
      | Password          | password |
      | Re-enter Password | password |
    Then I get an error registering a user of "Email Address is already registered"

  Scenario Outline: I successfully register a valid user: <Comment>
    Given I try to authenticate with an email address of "<Email>"
    When I try to register a user with details:
      | Name              | <Name>     |
      | Password          | <Password> |
      | Re-enter Password | <Password> |
    Then I am logged in as "<Name>"

    Examples: ASCII
      | Email                                        | Password                         | Name                             | Comment       |
      | Abc@example.com                              | superSecretPassword              | Test User                        | Simple        |
      | Abc.123@example.com                          | superSecretPassword123           | Test User 123                    | With numbers  |
      | user+mailbox-department=shipping@example.com | user+mailbox-department=shipping | user+mailbox-department=shipping | Complex ASCII |
      | !#$%&'*+-=?^_`.{}~@example.com               | !#$%&'*+-=?^_`.{}~               | !#$%&'*+-=?^_`.{}~               | Symbols       |

    Examples: Unicode
      | Email                      | Password | Name        | Comment           |
      | 用户@例子.广告                   | 用户       | 例子.广告       | Chinese, Unicode  |
      | अजय@डाटा.भारत              | अजय      | डाटा.भारत   | Hindi, Unicode    |
      | квіточка@пошта.укр         | квіточка | пошта.укр   | Ukranian, Unicode |
      | θσερ@εχαμπλε.ψομ           | θσερ     | εχαμπλε.ψομ | Greek, Unicode    |
      | Dörte@Sörensen.example.com | Dörte    | Sörensen    | German, Unicode   |
      | коля@пример.рф             | коля     | пример.рф   | Russian, Unicode  |
