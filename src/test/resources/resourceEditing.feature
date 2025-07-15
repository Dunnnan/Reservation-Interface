Feature: Resource Editing

  Background:
    Given Employee is authenticated
    And Employee is at the resource detail page
    And Employee is editing the resource

  Scenario: Employee changes the name of the resource
    When Employee enters a different name for the resource
    And Employee clicks the Edit button
    Then The resource name should be updated

  Scenario: Employee changes the description of the resource
    When Employee enters a different description for the resource
    And Employee clicks the Edit button
    Then The resource description should be updated

  Scenario: Employee changes the image of the resource
    When Employee uploads a new image for the resource
    And Employee clicks the Edit button
    Then The resource image should be updated

  Scenario: Employee changes the type of the resource
    When Employee selects a different type for the resource
    And Employee clicks the Edit button
    Then The resource type should be updated

  Scenario: Employee sets the resource as closed on a specific date
    When Employee selects a specific date
    And Employee marks the resource as closed
    Then The resource should not be available for reservations on that date

  Scenario: Employee updates the availability for a specific date
    When Employee selects a specific date
    And Employee changes the opening and closing times
    Then The resource availability should be updated for that date