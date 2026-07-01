# posts/posts.feature
# ─────────────────────────────────────────────────────────────────────────────
# Recurso: /posts — JSONPlaceholder
# Patrón:  Feature-per-Resource
# Tags:    @smoke → happy path | @regression → flujo completo | @negative → errores
# ─────────────────────────────────────────────────────────────────────────────

@posts
Feature: Gestión de Posts — JSONPlaceholder API

  Background:
    * url jsonplaceholderUrl
    * headers jsonHeaders

    # Schema derivado de respuesta real observada durante exploración
    * def postSchema =
      """
      {
        id:     '#number',
        title:  '#string',
        body:   '#string',
        userId: '#number'
      }
      """

  # ── GET /posts ─────────────────────────────────────────────────────────────

  @smoke @regression
  Scenario: GET /posts — listar todos los posts
    Given path '/posts'
    When method GET
    Then status 200
    And match response == '#[] postSchema'
    And match response[0].id == 1

  @smoke @regression
  Scenario: GET /posts/{id} — obtener post existente por ID
    Given path '/posts/1'
    When method GET
    Then status 200
    And match response == postSchema
    And match response.id == 1

  @negative
  Scenario: GET /posts/{id} — ID inexistente devuelve 404
    Given path '/posts/99999'
    When method GET
    Then status 404

  # ── POST /posts ────────────────────────────────────────────────────────────

  @smoke @regression
  Scenario: POST /posts — crear nuevo post con payload externo
    Given path '/posts'
    And request read('data/create-post.json')
    When method POST
    Then status 201
    And match response.id == '#number'
    And match response.title == 'Demo Karate Post'
    And match response.userId == 1

  @negative
  Scenario: POST /posts — body vacío devuelve 201 (JSONPlaceholder permisivo)
    Given path '/posts'
    And request {}
    When method POST
    Then status 201

  # ── PUT /posts/{id} ────────────────────────────────────────────────────────

  @regression
  Scenario: PUT /posts/{id} — reemplazar post completo
    Given path '/posts/1'
    And request { title: 'Reemplazado', body: 'Nuevo cuerpo completo', userId: 1 }
    When method PUT
    Then status 200
    And match response.title == 'Reemplazado'

  # ── PATCH /posts/{id} ──────────────────────────────────────────────────────

  @regression
  Scenario: PATCH /posts/{id} — actualizar campo parcial con payload externo
    Given path '/posts/1'
    And request read('data/update-post.json')
    When method PATCH
    Then status 200
    And match response.title == 'Post actualizado via PATCH'

  # ── DELETE /posts/{id} ─────────────────────────────────────────────────────

  @regression
  Scenario: DELETE /posts/{id} — eliminar post existente
    Given path '/posts/1'
    When method DELETE
    Then status 200
