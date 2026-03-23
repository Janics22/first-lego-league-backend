Feature: Manage Administrator
    In order to manage administrator users
    As an administrator
    I want to be able to create, retrieve, update and delete administrators

  Scenario: Create an administrator as admin
    Given I login as "admin" with password "password"
    When I create a new administrator with username "admin2", email "admin2@sample.app" and password "password"
    Then The response code is 201
    And It has been created an administrator with username "admin2" and email "admin2@sample.app"

  Scenario: Retrieve an administrator as admin
    Given I login as "admin" with password "password"
    When I retrieve the administrator with username "admin"
    Then The response code is 200
    And The retrieved administrator has email "admin@sample.app"

  Scenario: Delete an administrator as admin
    Given I login as "admin" with password "password"
    And There is an administrator with username "adminToDelete" and password "password" and email "delete@sample.app"
    When I delete the administrator with username "adminToDelete"
    Then The response code is 200
    And It has not been created an administrator with username "adminToDelete"

  Scenario: Create an administrator as regular user is forbidden
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    When I create a new administrator with username "admin3", email "admin3@sample.app" and password "password"
    Then The response code is 403

  Scenario: Create an administrator when not logged in is forbidden
    Given I'm not logged in
    When I create a new administrator with username "admin4", email "admin4@sample.app" and password "password"
    Then The response code is 401

  Scenario: Delete an administrator as regular user is forbidden
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    When I delete the administrator with username "admin"
    Then The response code is 403

  Scenario: Update an administrator as admin
    Given I login as "admin" with password "password"
    And There is an administrator with username "adminToUpdate" and password "password" and email "update@sample.app"
    When I update the administrator with username "adminToUpdate" with new email "adminToUpdate@sample.app"
    Then The response code is 200
    And It has been updated an administrator with username "adminToUpdate" and email "adminToUpdate@sample.app"
    
