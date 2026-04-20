Feature: Manage Referee REST API

  Background:
    Given I login as "admin" with password "password"
    And the volunteer system is empty

  Scenario: Create a referee
    When I request to create a referee with name "Jane Doe" and emailAddress "jane@udl.cat" and phoneNumber "123456789" and expert "true"
    Then the referee API response status should be 201
    And I request to retrieve that referee
    And the referee response should contain name "Jane Doe" and emailAddress "jane@udl.cat" and phoneNumber "123456789" and expert "true"

  Scenario: Retrieve a referee
    Given a referee exists with name "Marco" and emailAddress "marco@udl.cat" and phoneNumber "987654321" and expert "false"
    When I request to retrieve that referee
    Then the referee API response status should be 200
    And the referee response should contain name "Marco" and emailAddress "marco@udl.cat" and phoneNumber "987654321" and expert "false"

  Scenario: Update a referee
    Given a referee exists with name "Sofia" and emailAddress "sofia@udl.cat" and phoneNumber "555555555" and expert "false"
    When I request to update the referee name to "Sofia Updated"
    Then the referee API response status should be 204
    And I request to retrieve that referee
    Then the referee response should contain name "Sofia Updated" and emailAddress "sofia@udl.cat" and phoneNumber "555555555" and expert "false"

  Scenario: Delete a referee
    Given a referee exists with name "Alex" and emailAddress "alex@udl.cat" and phoneNumber "444444444" and expert "true"
    When I request to delete that referee
    Then the referee API response status should be 204
    And I request to retrieve that referee
    Then the referee API response status should be 404
