Feature: Manage ProjectRoom REST API

  Background:
    And the project room system is empty

  Scenario: Create a project room as admin
    When I request to create a project room with room number "R1"
    Then the project room API response status should be 201

  Scenario: Create a project room unauthenticated
    When I request to create a project room unauthenticated with room number "R2"
    Then the project room API response status should be 401

  Scenario: Retrieve an existing project room
    Given a project room "R3" exists
    When I request to retrieve project room "R3"
    Then the project room API response status should be 200
    And the response should contain room number "R3"

  Scenario: Retrieve a non-existent project room
    When I request to retrieve project room "nonExistent"
    Then the project room API response status should be 404

  Scenario: Delete a project room as admin
    Given a project room "R4" exists
    When I request to delete project room "R4"
    Then the project room API response status should be 204

  Scenario: Delete a project room unauthenticated
    Given a project room "R5" exists
    When I request to delete project room "R5" unauthenticated
    Then the project room API response status should be 401
