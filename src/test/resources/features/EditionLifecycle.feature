Feature: Edition Lifecycle
  As an admin
  I want to transition edition lifecycle states safely
  So that edition operations are available only when appropriate

  Background:
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "admin" with password "password"

  @HappyPath
  Scenario: Transition edition from DRAFT to OPEN
    Given There is an edition with year 2025, venue "Igualada" and description "FLL 2025"
    When I change the current edition state to "OPEN"
    Then The response code is 200
    And The edition transition response has previous state "DRAFT" and new state "OPEN"
    And The edition transition response status is "UPDATED"

  @HappyPath
  Scenario: Transition edition from OPEN to CLOSED
    Given There is an edition with year 2025, venue "Igualada" and description "FLL 2025"
    And The current edition state is changed to "OPEN"
    When I change the current edition state to "CLOSED"
    Then The response code is 200
    And The edition transition response has previous state "OPEN" and new state "CLOSED"
    And The edition transition response status is "UPDATED"

  @Validation
  Scenario: Cannot transition from CLOSED to OPEN
    Given There is an edition with year 2025, venue "Igualada" and description "FLL 2025"
    And The current edition state is changed to "OPEN"
    And The current edition state is changed to "CLOSED"
    When I change the current edition state to "OPEN"
    Then The response code is 409
    And The response has error "INVALID_EDITION_STATE_TRANSITION"

  @Validation
  Scenario: Cannot transition a non-existent edition
    When I change edition with id 9999 state to "OPEN"
    Then The response code is 404
    And The response has error "EDITION_NOT_FOUND"
