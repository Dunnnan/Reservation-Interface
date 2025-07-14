@e2e
Feature: Registration

  Background:
    Given user is on register page

  Scenario: User registers correctly
    Given password is the same as confirmPassword
    And the email is not registered yet
    When user enters name, surname, email, phoneNumber, password, confirmPassword
    And clicks Register button
    Then he should register and redirect to login page

  Scenario: Account with the inputted email already exists
    Given the email is already registered
    When uóser enters name, surname, email, phoneNumber, password, confirmPassword
    And clicks Register button
    Then he should see register failure message with annotation about incorrect email

  Scenario: Password and confirmPassword are not the same
    Given password is not the same as confirmPassword
    When user enters name, surname, email, phoneNumber, password, confirmPassword
    And clicks Register button
    Then he should see register failure message with annotation about incorrect password

  Scenario: User wants to login to account
    When user clicks Login button
    Then he should be transferred to the login page