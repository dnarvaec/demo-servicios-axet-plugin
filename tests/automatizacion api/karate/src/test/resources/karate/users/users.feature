@users
Feature: Gestion de Usuarios - JSONPlaceholder API

  Background:
    * url jsonplaceholderUrl
    * headers jsonHeaders

    * def userSchema =
      """
      {
        id:       '#number',
        name:     '#string',
        username: '#string',
        email:    '#string',
        address:  '#object',
        phone:    '#string',
        website:  '#string',
        company:  '#object'
      }
      """

  @smoke @regression
  Scenario: GET /users - listar todos los usuarios
    Given path '/users'
    When method GET
    Then status 200
    And match response == '#[] userSchema'
    And match response[0].id == 1

  @smoke @regression
  Scenario: GET /users/{id} - obtener usuario existente
    Given path '/users/1'
    When method GET
    Then status 200
    And match response == userSchema
    And match response.id == 1

  @negative
  Scenario: GET /users/{id} - usuario inexistente devuelve 404
    Given path '/users/99999'
    When method GET
    Then status 404

  @regression
  Scenario: GET /users/{id}/posts - posts de un usuario
    Given path '/users/1/posts'
    When method GET
    Then status 200
    And match response == '#array'
    And match response[0].userId == 1