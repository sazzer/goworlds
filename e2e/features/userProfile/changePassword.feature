Feature: User Profile: Change Password Form

  Background:
    Given a user exists with details:
      | Email Address | graham@grahamcox.co.uk |
      | Password      | superSecretPassword    |
      | Name          | Test User              |
    And I load the home page
    And I try to authenticate with an email address of "graham@grahamcox.co.uk"
    And I try to log in with a password of "superSecretPassword"
    And I am logged in as "Test User"

  Scenario Outline: The password can be changed successfully: <Comment>
    Given I open the Change Password page
    When I change the password to:
      | Password          | <Password>          |
      | Re-enter Password | <Re-enter Password> |
    Then The password is updated successfully

    Examples: ASCII
      | Password | Re-enter Password | Comment         |
      | simple   | simple            | Simple Password |

    Examples: Unicode
      | Password                   | Re-enter Password          | Comment           |
      | 用户@例子.广告                   | 用户@例子.广告                   | Chinese, Unicode  |
      | अजय@डाटा.भारत              | अजय@डाटा.भारत              | Hindi, Unicode    |
      | квіточка@пошта.укр         | квіточка@пошта.укр         | Ukranian, Unicode |
      | θσερ@εχαμπλε.ψομ           | θσερ@εχαμπλε.ψομ           | Greek, Unicode    |
      | Dörte@Sörensen.example.com | Dörte@Sörensen.example.com | German, Unicode   |
      | коля@пример.рф             | коля@пример.рф             | Russian, Unicode  |

  Scenario Outline: The user can log in with the new password: <Comment>
    Given I open the Change Password page
    And I change the password to:
      | Password          | <Password> |
      | Re-enter Password | <Password> |
    And The password is updated successfully
    And I log out
    When I try to authenticate with an email address of "graham@grahamcox.co.uk"
    And I try to log in with a password of "<Password>"
    Then I am logged in as "Test User"

    Examples: ASCII
      | Password | Comment         |
      | simple   | Simple Password |

    Examples: Unicode
      | Password                   | Comment           |
      | 用户@例子.广告                   | Chinese, Unicode  |
      | अजय@डाटा.भारत              | Hindi, Unicode    |
      | квіточка@пошта.укр         | Ukranian, Unicode |
      | θσερ@εχαμπλε.ψομ           | Greek, Unicode    |
      | Dörte@Sörensen.example.com | German, Unicode   |
      | коля@пример.рф             | Russian, Unicode  |

  Scenario: An error is displayed if the passwords do not match
    Given I open the Change Password page
    When I change the password to:
      | Password          | abc |
      | Re-enter Password | def |
    Then The Change Password Form has errors:
      | Re-enter Password | Passwords do not match |
