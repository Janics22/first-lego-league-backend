Feature: Manage Edition
    In order to manage editions
    As a user
    I want to be able to create, retrieve, update and delete editions

  Background:
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "admin" with password "password"

  Scenario: Create an edition
    When I create a new edition with year 2025, venue "Barcelona" and description "FLL Season 2025"
    Then The response code is 201
    And The edition has year 2025, venue "Barcelona" and description "FLL Season 2025"

  Scenario: Retrieve an edition
    Given There is an edition with year 2024, venue "Lleida" and description "FLL Season 2024"
    When I retrieve the edition
    Then The response code is 200
    And The edition has year 2024, venue "Lleida" and description "FLL Season 2024"

  Scenario: Update an edition
    Given There is an edition with year 2024, venue "Lleida" and description "FLL Season 2024"
    When I update the edition venue to "Tarragona"
    Then The response code is 200
    And The edition has venue "Tarragona"

  Scenario: Delete an edition
    Given There is an edition with year 2024, venue "Lleida" and description "FLL Season 2024"
    When I delete the edition
    Then The response code is 200
    And The edition has been deleted
