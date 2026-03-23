@MatchSearch
Feature: Search matches with filters

	Background:
		Given the database contains matches for search

	Scenario: Search without filters
		When I search matches with no filters
		Then the match response status should be 200

	Scenario: Search by table and round
		When I search matches with table "Table-01" and round 1
		Then the match response status should be 200
		And the response should contain matches

	Scenario: Search by time range
		When I search matches between "10:00:00" and "12:00:00"
		Then the match response status should be 200
		And the response should contain matches

	Scenario: Search using combined filters
		When I search matches with table "Table-01" between "10:00:00" and "12:00:00"
		Then the match response status should be 200
		And the response should contain matches

	Scenario: Invalid time range
		When I search matches between "14:00:00" and "10:00:00"
		Then the response status should be 422
		And the error code should be "INVALID_TIME_FILTER_RANGE"

	@FindByTeam
	Scenario: Retrieve matches for a team
		Given a team "LegoStars" exists with matches
		When I search matches for team "/teams/LegoStars"
		Then the match response status should be 200
		And the team matches response should contain matches for "LegoStars"

	@FindByTeam
	Scenario: Returns empty list when team has no matches
		Given a team "EmptyTeam" exists with no matches
		When I search matches for team "/teams/EmptyTeam"
		Then the match response status should be 200
		And the team matches response should be empty

	@FindByTeam
	Scenario: Returns error when team does not exist
		When I search matches for team "/teams/NonExistentTeam"
		Then the match response status should be 404
		And the team matches error should be "TEAM_NOT_FOUND"