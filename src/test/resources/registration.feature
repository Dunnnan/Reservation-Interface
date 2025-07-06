Feature: Registration

  Scenario: User registers correctly
    Given user is on registering page
    When user enters name, surname, email, password, confirmPassword
    And clicks Register button
    And the email is not registered yet
    And password is the same as confirmPassword
    Then he should register and redirect to home page

  Scenario: Account with the inputted email already exists
    Given user is on registering page
    When user enters name, surname, email, password, confirmPassword
    And clicks Register button
    And the email is already registered
    Then he should see register failure message with annotation about incorrect email

  Scenario: Password and confirmPassword are not the same
    Given user is on registering page
    When user enters name, surname, email, password, confirmPassword
    And clicks Register button
    And password is not the same as confirmPassword
    Then he should see register failure message with annotation about incorrect password

  Scenario: User wants to login to account
    Given user is on register page
    When user clicks Login button
    Then he should be transferred to the login page