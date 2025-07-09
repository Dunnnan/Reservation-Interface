Feature: Resource Reservation

  Background:
    Given User is authenticated
    And User is at resource's dedicated page

  Scenario: User reserve resource successfully
    When User picks a certain day of reservation
    And User picks a from hour of reservation
    And User picks a to hour of reservation
    And picked term is free
    Then User should reserve resource

  Scenario: User reserve resource unsuccessfully
    When User picks a certain day of reservation
    And User picks a from hour of reservation
    And User picks a to hour of reservation
    And picked term is full
    Then User should not reserve resource
    And see the error message "Cannot reserve occupied term"

  Scenario: User reserve resource in the past
    When User picks a day of reservation in the past
    Then User should not be able to reserve resource
    And see the error message "Cannot reserve resource in the past"

  Scenario: User selects invalid reservation time range
    When User picks a from hour later than the to hour
    Then User should not be able to reserve resource
    And see the error message "Invalid time range"