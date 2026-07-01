# features/posts/posts.feature
# ─────────────────────────────────────────────────────────────────────────────
# Recurso: /posts — JSONPlaceholder API
# Patrón:  Screenplay Pattern (Actor → Task → Question)
# ─────────────────────────────────────────────────────────────────────────────

@posts
Feature: Gestión de Posts — JSONPlaceholder API

  Como equipo de QA
  Quiero validar los endpoints del recurso Posts
  Para asegurar que la API responde correctamente a operaciones CRUD

  Background:
    Given el actor puede consumir la API de JSONPlaceholder

  # ── GET ───────────────────────────────────────────────────────────────────

  @smoke @regression
  Scenario: Obtener todos los posts
    When obtiene todos los posts
    Then la respuesta contiene una lista de posts

  @smoke @regression
  Scenario: Obtener un post existente por ID
    When obtiene el post con ID 1
    Then el status code de la respuesta es 200
    And el post tiene ID 1
    And el post tiene un título no nulo

  @negative
  Scenario: Obtener un post con ID inexistente devuelve 404
    When obtiene el post con ID 99999
    Then el status code de la respuesta es 404

  # ── POST ──────────────────────────────────────────────────────────────────

  @smoke @regression
  Scenario: Crear un nuevo post con título personalizado
    When crea un nuevo post con título "Demo Serenity Screenplay"
    Then el post fue creado con un ID asignado

  @regression
  Scenario: Crear un post con datos por defecto
    When crea un post con los datos por defecto
    Then el post fue creado con un ID asignado

  # ── PATCH ─────────────────────────────────────────────────────────────────

  @regression
  Scenario: Actualizar parcialmente el título de un post
    When actualiza el post 1 con título "Título actualizado via Screenplay"
    Then el status code de la respuesta es 200

  # ── DELETE ────────────────────────────────────────────────────────────────

  @regression
  Scenario: Eliminar un post existente devuelve 200
    When elimina el post con ID 1
    Then el status code de la respuesta es 200
