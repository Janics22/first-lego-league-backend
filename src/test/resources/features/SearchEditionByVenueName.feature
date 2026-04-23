Feature: Search Edition by venue name

	Background:
		Given I login as "admin" with password "password"
		And the volunteer system is empty
		Given an edition exists with year 2024, venue name "Palau Sant Jordi" and description "FLL 2024"

	Scenario: Search returns editions with exact venue name match
		When I search for an edition by venue name "Palau Sant Jordi"
		Then the edition search response status should be 200
		And the edition search response should contain an edition with venue name "Palau Sant Jordi"

	Scenario: Search returns editions with partial venue name match (case-insensitive)
		When I search for an edition by venue name "palau"
		Then the edition search response status should be 200
		And the edition search response should contain an edition with venue name "Palau Sant Jordi"

	Scenario: Search returns empty list when no match
		When I search for an edition by venue name "Unknown"
		Then the edition search response status should be 200
		And the edition search response should be empty