Feature: Edition Competition Tables Overview

  Scenario: Authenticated user retrieves competition tables with scheduled matches for one edition
    Given I login as "admin" with password "password"
    And an edition competition table dataset exists
    When I request competition tables for the target edition
    Then The response code is 200
    And the competition table overview contains 1 table with 2 matches
    And the competition table overview includes the target table identifier
    And the competition table overview includes match times "11:00" and "11:20"
    And the competition table overview does not include tables from other editions

  Scenario: Authenticated user retrieves an empty table list for an edition without tables
    Given I login as "admin" with password "password"
    And an edition without competition tables exists
    When I request competition tables for the target edition
    Then The response code is 200
    And the competition table overview contains 0 table with 0 matches

  Scenario: Retrieving tables for a non-existing edition returns not found
    Given I login as "admin" with password "password"
    When I request competition tables for edition id 999999
    Then The response code is 404
    And the competition table overview error is "EDITION_NOT_FOUND"

  Scenario: Retrieving tables excludes non-scheduled matches
    Given I login as "admin" with password "password"
    And an edition competition table dataset with non-scheduled matches exists
    When I request competition tables for the target edition
    Then The response code is 200
    And the competition table overview contains 1 table with 1 matches
    And the competition table overview includes the target table identifier
    And the competition table overview does not include tables from other editions
