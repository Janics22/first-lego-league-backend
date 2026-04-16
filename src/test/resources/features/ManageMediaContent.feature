Feature: Manage MediaContent
    In order to manage media content
    As a user
    I want to be able to create, retrieve, update and delete media content

  Background:
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "admin" with password "password"

  Scenario: Create a media content
    When I create a new media content with url "photo123" and type "image"
    Then The response code is 201
    And The created media content has type "image"

  Scenario: Retrieve a media content
    Given There is a media content with url "video456" and type "video"
    When I retrieve the media content with url "video456"
    Then The response code is 200
    And The retrieved media content has type "video"

  Scenario: Update a media content type
    Given There is a media content with url "file789" and type "document"
    When I update the media content with url "file789" type to "pdf"
    Then The response code is 200
    And The retrieved media content has type "pdf"

  Scenario: Delete a media content
    Given There is a media content with url "old101" and type "image"
    When I delete the media content with url "old101"
    Then The response code is 200
    And The media content with url "old101" has been deleted

  Scenario: Create media content unauthenticated returns 401
    Given I'm not logged in
    When I create a new media content with url "photo-unauth" and type "image"
    Then The response code is 401

  Scenario: Create media content as regular user returns 403
    Given I login as "user" with password "password"
    When I create a new media content with url "photo-user" and type "image"
    Then The response code is 403

  Scenario: Delete media content unauthenticated returns 401
    Given There is a media content with url "del-unauth" and type "image"
    And I'm not logged in
    When I delete the media content with url "del-unauth"
    Then The response code is 401

  Scenario: Delete media content as regular user returns 403
    Given There is a media content with url "del-user" and type "image"
    And I login as "user" with password "password"
    When I delete the media content with url "del-user"
    Then The response code is 403
