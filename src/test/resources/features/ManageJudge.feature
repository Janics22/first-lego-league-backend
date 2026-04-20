Feature: Manage Judge REST API

  Background:
    Given I login as "admin" with password "password"
    And the volunteer system is empty

  Scenario: Create a judge
    When I request to create a judge with name "Jordi" and emailAddress "jordi@udl.cat" and phoneNumber "123456789" and expert "true"
	Then the judge API response status should be 201
	And I request to retrieve that judge
    And the judge response should contain name "Jordi" and emailAddress "jordi@udl.cat" and phoneNumber "123456789" and expert "true"

  Scenario: Retrieve a judge
    Given a judge exists with name "Marc" and emailAddress "marc@udl.cat" and phoneNumber "123456789" and expert "false"
    When I request to retrieve that judge
    Then the judge API response status should be 200
    And the judge response should contain name "Marc" and emailAddress "marc@udl.cat" and phoneNumber "123456789" and expert "false"

  Scenario: Update a judge
    Given a judge exists with name "Anna" and emailAddress "anna@udl.cat" and phoneNumber "123456789" and expert "false"
    When I request to update the judge name to "Anna Updated"
    Then the judge API response status should be 204
    And I request to retrieve that judge 
    Then the judge response should contain name "Anna Updated" and emailAddress "anna@udl.cat" and phoneNumber "123456789" and expert "false"

  Scenario: Delete a judge
    Given a judge exists with name "Joan" and emailAddress "joan@udl.cat" and phoneNumber "123456789" and expert "false"
    When I request to delete that judge
    Then the judge API response status should be 204
    And I request to retrieve that judge
    Then the judge API response status should be 404
