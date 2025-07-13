Feature: Registration

  Background:
    Given user is on register page

  Scenario: User registers correctly
    When user enters name, surname, email, phoneNumber, password, confirmPassword
    And clicks Register button
    And the email is not registered yet
    And password is the same as confirmPassword
    Then he should register and redirect to home page

  Scenario: Account with the inputted email already exists
    When user enters name, surname, email, phoneNumber, password, confirmPassword
    And clicks Register button
    And the email is already registered
    Then he should see register failure message with annotation about incorrect email

  Scenario: Password and confirmPassword are not the same
    When user enters name, surname, email, phoneNumber, password, confirmPassword
    And clicks Register button
    And password is not the same as confirmPassword
    Then he should see register failure message with annotation about incorrect password

  Scenario: User wants to login to account
    When user clicks Login button
    Then he should be transferred to the login page