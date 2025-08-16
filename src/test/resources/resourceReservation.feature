@e2e
Feature: Resource Reservation

  Background:
    Given User is authenticated
    And User is at resource's dedicated page

  Scenario: User reserve resource successfully
    When User picks a present or future day of reservation
    And User picks a from hour of reservation
    And User picks a to hour of reservation
    And picked term is free
    And User clicks reserve button
    Then User should reserve resource

  Scenario: User reserve resource for time period that already past
    When User picks a present day of reservation
    And User picks a from hour of reservation
    And User picks a to hour of reservation
    And picked period of time is in the past
    And User clicks reserve button
    Then User should not reserve resource
    And see the error message

  Scenario: User reserve resource already occupied
    When User picks a present or future day of reservation
    And User picks a from hour of reservation
    And User picks a to hour of reservation
    And picked term is full
    And User clicks reserve button
    Then User should not reserve resource
    And see the error message

  Scenario: User reserve resource in the past
    When User picks a past day of reservation
    And User picks a from hour of reservation
    And User picks a to hour of reservation
    Then User should not reserve resource
    And User clicks reserve button
    And see the error message

  Scenario: User selects invalid reservation time range
    When User picks a present or future day of reservation
    And User picks a from hour of reservation
    And User picks a to hour of reservation
    And from hour is later than the to hour
    And User clicks reserve button
    Then User should not be able to reserve resource
    And see the error message