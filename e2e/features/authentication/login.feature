Feature: Authentication: Logging in

  Background:
    Given I load the home page

  Scenario: An error is displayed if no password is entered
    Given a user exists with details:
      | Email Address | graham@grahamcox.co.uk |
      | Password      | superSecretPassword    |
      | Name          | Test User              |
    And I try to authenticate with an email address of "graham@grahamcox.co.uk"
    When I try to log in with a password of ""
    Then I get errors logging in:
      | Password | Please enter a password |

  Scenario: An error is displayed if the wrong password is entered
    Given a user exists with details:
      | Email Address | graham@grahamcox.co.uk |
      | Password      | superSecretPassword    |
      | Name          | Test User              |
    And I try to authenticate with an email address of "graham@grahamcox.co.uk"
    When I try to log in with a password of "wrongPassword"
    Then I get an error logging in of "Invalid password or account blocked"

  Scenario Outline: I successfully log in as a valid user
    Given a user exists with details:
      | Email Address | <Email>    |
      | Password      | <Password> |
      | Name          | <Name>     |
    And I try to authenticate with an email address of "<Email>"
    When I try to log in with a password of "<Password>"
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
