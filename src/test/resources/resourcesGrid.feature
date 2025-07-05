Feature: Resources Grid

  Scenario: User enters the dedicated page of a resource
    Given User is authenticated
    And User is at the home page
    When User clicks on a resource card
    Then User should be redirected to the resource's dedicated page

  Scenario: User navigates between pages of resources
    Given User is authenticated
    And User is at the home page
    When User clicks on a pagination control
    Then The resource grid should update with the next page of results

  Scenario: User searches for a specific resource
    Given User is authenticated
    And User is at the home page
    When User enters a search phrase into the search bar
    And User submits the search
    Then The resource grid should update to show results

  Scenario: User filters resources by parameter
    Given User is authenticated
    And User is at the home page
    When User picks a filter parameter from the list
    Then The resource grid should update to show filtered results

  Scenario: User sorts resources by parameter
    Given User is authenticated
    And User is at the home page
    When User picks a sort parameter from the list
    Then The resource grid should update to show sorted results

  Scenario: Employee opens the add resource form
    Given User is authenticated as an Employee
    And User is at the home page
    When User clicks the "Add" button
    Then The add resource pop-up should appear

  Scenario: Employee submits the add resource form
    Given The add resource pop-up is open
    When User fills in valid data
    And User submits the form
    Then The resource should be added to the grid
    And The pop-up should close