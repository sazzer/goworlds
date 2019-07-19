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

  Scenario Outline: The User Profile Form can be saved correctly: <Comment>
    Given I open the User Profile page
    When I update the User Profile form to:
      | Email Address | <Email> |
      | Name          | <Name>  |
    Then The User Profile Form has details:
      | Email Address | <Email> |
      | Name          | <Name>  |
    And I am logged in as "<Name>"

    Examples: ASCII
      | Email                                        | Name                             | Comment       |
      | Abc@example.com                              | Test User                        | Simple        |
      | Abc.123@example.com                          | Test User 123                    | With numbers  |
      | user+mailbox-department=shipping@example.com | user+mailbox-department=shipping | Complex ASCII |
      | !#$%&'*+-=?^_`.{}~@example.com               | !#$%&'*+-=?^_`.{}~               | Symbols       |

    Examples: Unicode
      | Email                      | Name        | Comment           |
      | 用户@例子.广告                   | 例子.广告       | Chinese, Unicode  |
      | अजय@डाटा.भारत              | डाटा.भारत   | Hindi, Unicode    |
      | квіточка@пошта.укр         | пошта.укр   | Ukranian, Unicode |
      | θσερ@εχαμπλε.ψομ           | εχαμπλε.ψομ | Greek, Unicode    |
      | Dörte@Sörensen.example.com | Sörensen    | German, Unicode   |
      | коля@пример.рф             | пример.рф   | Russian, Unicode  |

  Scenario Outline: Changes to the User Profile form are persisted correctly: <Comment>
    Given I open the User Profile page
    And I update the User Profile form to:
      | Email Address | <Email> |
      | Name          | <Name>  |
    When I open the Change Password page
    And I open the User Profile page
    Then The User Profile Form has details:
      | Email Address | <Email> |
      | Name          | <Name>  |

    Examples: ASCII
      | Email                                        | Name                             | Comment       |
      | Abc@example.com                              | Test User                        | Simple        |
      | Abc.123@example.com                          | Test User 123                    | With numbers  |
      | user+mailbox-department=shipping@example.com | user+mailbox-department=shipping | Complex ASCII |
      | !#$%&'*+-=?^_`.{}~@example.com               | !#$%&'*+-=?^_`.{}~               | Symbols       |

    Examples: Unicode
      | Email                      | Name        | Comment           |
      | 用户@例子.广告                   | 例子.广告       | Chinese, Unicode  |
      | अजय@डाटा.भारत              | डाटा.भारत   | Hindi, Unicode    |
      | квіточка@пошта.укр         | пошта.укр   | Ukranian, Unicode |
      | θσερ@εχαμπλε.ψομ           | εχαμπλε.ψομ | Greek, Unicode    |
      | Dörte@Sörensen.example.com | Sörensen    | German, Unicode   |
      | коля@пример.рф             | пример.рф   | Russian, Unicode  |

  Scenario Outline: Appropriate errors are displayed when the form is not filled out correctly: <Comment>
    Given I open the User Profile page
    When I update the User Profile form to:
      | Email Address | <Email> |
      | Name          | <Name>  |
    Then The User Profile Form has errors:
      | Email Address | <Email Error> |
      | Name          | <Name Error>  |
    And I am logged in as "Test User"
    And I open the Change Password page
    And I open the User Profile page
    And The User Profile Form has details:
      | Email Address | graham@grahamcox.co.uk |
      | Name          | Test User              |

    Examples:
      | Name   | Email                  | Name Error               | Email Error                        | Comment             |
      |        |                        | Please enter a user name | Please enter an Email Address      | No fields populated |
      |        | graham@grahamcox.co.uk | Please enter a user name |                                    | Name not populated  |
      | Graham |                        |                          | Please enter an Email Address      | Email not populated |
      | Graham | invalid                |                          | Please enter a valid Email Address | Email not valid     |

    Scenario: An error occurs when the new email address is a duplicate
      Given a user exists with details:
        | Email Address | duplicate@example.com |
      And I open the User Profile page
      When I update the User Profile form to:
        | Email Address | duplicate@example.com |
      Then I get an error updating the user profile of "Email Address is already registered"
      And I am logged in as "Test User"
      And I open the Change Password page
      And I open the User Profile page
      And The User Profile Form has details:
        | Email Address | graham@grahamcox.co.uk |
        | Name          | Test User              |
