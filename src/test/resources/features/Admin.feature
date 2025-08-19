Feature: Admin user management

  Scenario: Add and delete user
    Given User logs in with username "Admin" and password "admin123"
    When User navigates to Admin tab
    And User adds a new user
    Then User count should increase by 1