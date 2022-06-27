Feature:  Tests to cover searching repositories by author name, with pagination and sorting, as an unauthenticated user

  @pagination
  Scenario Outline: Testing pagination for search authors
    Given User sends api call with <users> users per page
    Then User validates status code 200
    And User validates result contains <expectedUsers> users only
    Examples:
    | users | expectedUsers |
    | 3     |3     |
    | 0     |30    |
    | 99    |99    |
    | 120   |100   |
    | -1    |30    |

    @sorting
  Scenario: Testing sorting for search authors
    Given User sends api call for users with sorting "followers" and order "asc"
    Then User validates status code 200
    And User validates result response body contains "followers" in "asc" order
