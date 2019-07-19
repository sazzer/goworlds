Feature: Authentication: Checking Email Addresses

  Background:
    Given I load the home page

  Scenario: An error is displayed if no email address is entered
    When I try to authenticate with an email address of ""
    Then I get an error entering the email address of "Please enter an Email Address"

  Scenario: An error is displayed if an invalid email address is entered
    When I try to authenticate with an email address of "invalid"
    Then I get an error entering the email address of "Please enter a valid Email Address"

  Scenario Outline: The Register User screen is displayed if an unknown email address is entered: <Comment>
    When I try to authenticate with an email address of "<Email>"
    Then the User Registration form is displayed

    Examples: ASCII
      | Email               | Comment      |
      | Abc@example.com     | Simple       |
      | Abc.123@example.com | With numbers |
#      | user+mailbox/department=shipping@example.com | Complex ASCII       |
#      | !#$%&'*+-/=?^_`.{}~@example.com              | Symbols             |

    Examples: Unicode
      | Email                      | Comment           |
      | 用户@例子.广告                   | Chinese, Unicode  |
      | अजय@डाटा.भारत              | Hindi, Unicode    |
      | квіточка@пошта.укр         | Ukranian, Unicode |
      | θσερ@εχαμπλε.ψομ           | Greek, Unicode    |
      | Dörte@Sörensen.example.com | German, Unicode   |
      | коля@пример.рф             | Russian, Unicode  |

  @ignore
    Examples: Manual testing only - quotes don't work in the gherkin
      | Email                     | Comment             |
      | "Abc@def"@example.com     | Quoted              |
      | "Fred Bloggs"@example.com | Quoted with spaces  |
      | "Joe.\\Blow"@example.com  | Quoted with symbols |


  Scenario Outline: The Login screen is displayed if a known email address is entered: <Comment>
    Given a user exists with details:
      | Email Address | <Email> |
    When I try to authenticate with an email address of "<Email>"
    Then the Login form is displayed

    Examples: ASCII
      | Email                                        | Comment       |
      | Abc@example.com                              | Simple        |
      | Abc.123@example.com                          | With numbers  |
      | user+mailbox-department=shipping@example.com | Complex ASCII |
      | !#$%&'*+-=?^_`.{}~@example.com               | Symbols       |

    Examples: Unicode
      | Email                      | Comment           |
      | 用户@例子.广告                   | Chinese, Unicode  |
      | अजय@डाटा.भारत              | Hindi, Unicode    |
      | квіточка@пошта.укр         | Ukranian, Unicode |
      | θσερ@εχαμπλε.ψομ           | Greek, Unicode    |
      | Dörte@Sörensen.example.com | German, Unicode   |
      | коля@пример.рф             | Russian, Unicode  |

  @ignore
    Examples: Manual testing only - quotes don't work in the gherkin
      | Email                     | Comment             |
      | "Abc@def"@example.com     | Quoted              |
      | "Fred Bloggs"@example.com | Quoted with spaces  |
      | "Joe.\\Blow"@example.com  | Quoted with symbols |
