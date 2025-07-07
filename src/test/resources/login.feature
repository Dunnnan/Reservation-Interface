@e2e
Feature: Login

  Background:
    Given user is on logging page

  Scenario: User logs correctly
    When user enters correct email and password
    And clicks Login button
    Then he should login and redirect to home page

  Scenario: User logs incorrectly
    When puts in incorrect email or password
    And clicks Login button
    Then he should see the logging failure message

  Scenario: User wants to create account
    Given user is on logging page
    When clicks Create account button
    Then he should be transferred to the register page