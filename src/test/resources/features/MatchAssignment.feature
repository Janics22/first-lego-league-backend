Feature: Match referee assignment

	Background:
		Given the match assignment system is empty
		And There is a registered user with username "user" and password "password" and email "user@sample.app"
		And I login as "user" with password "password"

	Scenario: Assign referee to match successfully
		Given a match with state "SCHEDULED" exists from "2026-03-01T10:00:00" to "2026-03-01T11:00:00"
		And a referee volunteer exists
		When I assign that referee to that match
		Then The response code is 200
		And the assignment response status is "ASSIGNED"

	Scenario: Match not found
		Given a referee volunteer exists
		When I assign referee id "1" to match id "99999"
		Then The response code is 404
		And assignment error code is "MATCH_NOT_FOUND"

	Scenario: Referee not found
		Given a match with state "SCHEDULED" exists from "2026-03-01T10:00:00" to "2026-03-01T11:00:00"
		When I assign referee id "99999" to that match
		Then The response code is 404
		And assignment error code is "REFEREE_NOT_FOUND"

	Scenario: Invalid role
		Given a match with state "SCHEDULED" exists from "2026-03-01T10:00:00" to "2026-03-01T11:00:00"
		And a floater volunteer exists
		When I assign that floater to that match
		Then The response code is 422
		And assignment error code is "INVALID_ROLE"

	Scenario: Match already has referee
		Given a referee volunteer exists
		And another referee volunteer exists
		And a match with state "SCHEDULED" exists from "2026-03-01T10:00:00" to "2026-03-01T11:00:00" assigned to the first referee
		When I assign the second referee to that match
		Then The response code is 409
		And assignment error code is "MATCH_ALREADY_HAS_REFEREE"

	Scenario: Invalid match state
		Given a match with state "FINISHED" exists from "2026-03-01T10:00:00" to "2026-03-01T11:00:00"
		And a referee volunteer exists
		When I assign that referee to that match
		Then The response code is 422
		And assignment error code is "INVALID_MATCH_STATE"

	Scenario: Availability conflict
		Given a referee volunteer exists
		And a match with state "SCHEDULED" exists from "2026-03-01T10:00:00" to "2026-03-01T11:00:00" assigned to the referee
		And a match with state "SCHEDULED" exists from "2026-03-01T10:30:00" to "2026-03-01T11:30:00"
		When I assign that referee to that match
		Then The response code is 409
		And assignment error code is "AVAILABILITY_CONFLICT"

	Scenario: Invalid ID format
		When I assign referee id "abc" to match id "xyz"
		Then The response code is 422
		And assignment error code is "INVALID_ID_FORMAT"
