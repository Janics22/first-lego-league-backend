Feature: Admin Access Control
    In order to protect the system
    As a system designer
    I want modifications to be only callable by administrators while reads are public

  Scenario: Anonymous user can read editions
    Given I login as "admin" with password "password"
    And There is an edition with year 2024, venue "TestVenue" and description "Test"
    And I'm not logged in
    When I retrieve the editions list
    Then The response code is 200

  Scenario: Regular user cannot create an edition
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    When I create a new edition with year 2025, venue "Barcelona" and description "FLL Season 2025"
    Then The response code is 403

  Scenario: Admin can create an edition
    Given I login as "admin" with password "password"
    When I create a new edition with year 2025, venue "Barcelona" and description "FLL Season 2025"
    Then The response code is 201

  Scenario: Anonymous user cannot create an edition
    Given I'm not logged in
    When I create a new edition with year 2026, venue "Madrid" and description "FLL Season 2026"
    Then The response code is 401

  Scenario: Anonymous user cannot delete an edition
    Given I login as "admin" with password "password"
    And There is an edition with year 2024, venue "Lleida" and description "FLL Season 2024"
    And I'm not logged in
    When I delete the edition
    Then The response code is 401
