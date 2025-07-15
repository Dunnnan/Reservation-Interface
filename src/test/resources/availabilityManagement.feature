Feature: Availability Management

  Background:
    Given Employee is authenticated
    And Employee is on the availability page

  Scenario: Employee changes the default availability period for all resources
    When Employee chooses the option to change the default availability period
    And Employee sets new opening and closing times
    Then The default availability period is updated for all resources

  Scenario: Employee changes availability for selected resources during a specific period
    When Employee selects one or more resource types
    And Employee selects a time period
    And Employee sets new opening and closing times
    Then The availability is updated for the selected resources during that period

  Scenario: Employee marks selected resources as closed during a specific period
    When Employee selects one or more resource types
    And Employee selects a time period
    And Employee marks the resources as closed
    Then The selected resources should not be available for reservations during that period