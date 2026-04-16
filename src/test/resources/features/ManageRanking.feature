Feature: Manage Rankings
  As a competition organizer
  I want to retrieve rankings via the API
  So that I can see team standings

  Scenario: List all rankings returns 200
    Given I'm not logged in
    And rankings exist in the system
    When I request the rankings list
    Then The response code is 200

  Scenario: Retrieve ranking by id returns 200 with correct fields
    Given I'm not logged in
    And a ranking exists for team "TeamA" with position 1 and totalScore 450
    When I retrieve that ranking by id
    Then The response code is 200
    And the response contains position 1
    And the response contains totalScore 450

  Scenario: Retrieve non-existent ranking returns 404
    Given I'm not logged in
    When I retrieve ranking with id 999999
    Then The response code is 404
