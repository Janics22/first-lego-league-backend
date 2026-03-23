Feature: Search Rounds by Edition
	As a tournament organizer
	I want to retrieve all rounds belonging to a specific edition
	So that I can manage the tournament round structure

	Scenario: Retrieve all rounds for an edition
		Given I login as "admin" with password "password"
		And An edition exists with year 2025 and venue "Test Venue" and description "Round search test"
		And A round with number 101 exists for that edition
		And A round with number 102 exists for that edition
		When I search rounds by the edition id
		Then The response code is 200
		And The round search response should contain 2 rounds
		And The round search response should include round with number 101
		And The round search response should include round with number 102

	Scenario: Returns empty list when edition has no rounds
		Given I login as "admin" with password "password"
		And An edition exists with year 2026 and venue "Empty Venue" and description "No rounds edition"
		When I search rounds by the edition id
		Then The response code is 200
		And The round search response should contain 0 rounds

