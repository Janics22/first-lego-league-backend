Feature: Manage Scientific Project
    In order to manage scientific project evaluations
    As a user
    I want to be able to create and search scientific projects

    Scenario: Create a scientific project with associated team and edition
        Given There is a registered user with username "user" and password "password" and email "user@sample.app"
        And I login as "admin" with password "password"
        When I create a new scientific project with score 85 and comments "Great innovation" for team "LegoStars" and a valid edition
        Then The response code is 201
        And The response has a team link
        And The response has an edition link
        And The latest scientific project has a team relation endpoint
        And The latest scientific project has an edition relation endpoint

    Scenario: Reject a scientific project without team
        Given There is a registered user with username "user" and password "password" and email "user@sample.app"
        And I login as "admin" with password "password"
        When I create a new scientific project with score 80 and comments "Missing team" without team and with valid edition
        Then The response code is 400
        And The error code is "TEAM_REQUIRED"
        And The error message is "A scientific project must have an associated team"

    Scenario: Reject a scientific project without edition
        Given There is a registered user with username "user" and password "password" and email "user@sample.app"
        And I login as "admin" with password "password"
        When I create a new scientific project with score 81 and comments "Missing edition" without edition and with valid team "EditionlessTeam"
        Then The response code is 400
        And The error code is "EDITION_REQUIRED"
        And The error message is "A scientific project must belong to an edition"

    Scenario: Reject a scientific project with invalid edition reference
        Given There is a registered user with username "user" and password "password" and email "user@sample.app"
        And I login as "admin" with password "password"
        When I create a new scientific project with score 82 and comments "Unknown edition" and invalid edition for team "EditionGhosts"
        Then The response code is 400
        And The error code is "EDITION_NOT_FOUND"
        And The error message is "The referenced edition does not exist"

    Scenario: Reject a scientific project when team is not registered in edition
        Given There is a registered user with username "user" and password "password" and email "user@sample.app"
        And I login as "admin" with password "password"
        When I create a new scientific project with score 83 and comments "Team edition mismatch" for unregistered team "MismatchTeam"
        Then The response code is 400
        And The error code is "EDITION_TEAM_MISMATCH"
        And The error message is "The referenced team is not registered in the referenced edition"

    Scenario: Reject a scientific project with invalid team reference
        Given There is a registered user with username "user" and password "password" and email "user@sample.app"
        And I login as "admin" with password "password"
        When I create a new scientific project with score 84 and comments "Unknown team" and invalid team with valid edition
        Then The response code is 400
        And The error code is "TEAM_NOT_FOUND"
        And The error message is "The referenced team does not exist"

    Scenario: Find scientific projects by minimum score
        Given There is a registered user with username "user" and password "password" and email "user@sample.app"
        And I login as "user" with password "password"
        And There is a scientific project with score 90 and comments "Excellent research" for team "AlphaTeam" and a valid edition
        When I search for scientific projects with minimum score 85
        Then The response code is 200
        And The response contains 1 scientific project(s)

    Scenario: Find scientific projects with minimum score returns no results
        Given There is a registered user with username "user" and password "password" and email "user@sample.app"
        And I login as "user" with password "password"
        And There is a scientific project with score 70 and comments "Average work" for team "BetaTeam" and a valid edition
        When I search for scientific projects with minimum score 85
        Then The response code is 200
        And The response contains 0 scientific project(s)

    Scenario: Find scientific projects by team name
        Given There is a registered user with username "user" and password "password" and email "user@sample.app"
        And I login as "user" with password "password"
        And There is a scientific project with score 88 and comments "Robotics focus" for team "SearchTeam" and a valid edition
        And There is a scientific project with score 75 and comments "Another team project" for team "OtherTeam" and a valid edition
        When I search for scientific projects by team name "SearchTeam"
        Then The response code is 200
        And The response contains 1 scientific project(s)

    Scenario: Find scientific projects by edition id
        Given There is a registered user with username "user" and password "password" and email "user@sample.app"
        And I login as "user" with password "password"
        And There is a scientific project with score 91 and comments "Target edition project" for team "EditionSearchA" in a tracked edition
        And There is a scientific project with score 67 and comments "Other edition project" for team "EditionSearchB" and a valid edition
        When I search for scientific projects by the tracked edition
        Then The response code is 200
        And The response contains 1 scientific project(s)
