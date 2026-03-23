Feature: Manage Match Results
  As a developer
  I want to verify that the match results repository is exposed correctly

  Scenario: MatchResults endpoint is working
    Given I'm not logged in
    When I request the match results list
    Then The response code is 200

  Scenario: Direct MatchResult creation is disabled
    Given There is a registered user with username "admin" and password "password" and email "admin@fll.udl.cat"
    And I login as "admin" with password "password"
    And The dependencies exist
    When I create a match result with score 10
    Then The response code is 405
