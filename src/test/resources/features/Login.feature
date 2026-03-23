Feature: Login
    In order to access the application
    As an administrator
    I want to be able to login to the system

  Scenario: Login as administrator with correct credentials
    Given I login as "admin" with password "password"
    When I check my identity
    Then The response code is 200
    And The identity username is "admin"

  Scenario: Login with wrong password
    Given I login as "admin" with password "wrongpassword"
    When I check my identity
    Then The response code is 401

  Scenario: Login with non-existing user
    Given I login as "nonexistent" with password "password"
    When I check my identity
    Then The response code is 401

  Scenario: Access identity without login
    Given I'm not logged in
    When I check my identity
    Then The response code is 401

  Scenario: Login as regular user with correct credentials
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    When I check my identity
    Then The response code is 200
    And The identity username is "user"
