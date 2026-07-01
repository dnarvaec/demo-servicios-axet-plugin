# features/todos/todos.feature
# ─────────────────────────────────────────────────────────────────────────────
# Recurso: /todos — JSONPlaceholder API
# Patrón:  Screenplay Pattern (Actor → Task → Question)
# ─────────────────────────────────────────────────────────────────────────────

@todos
Feature: Gestión de Todos — JSONPlaceholder API

  Como equipo de QA
  Quiero validar los endpoints del recurso Todos
  Para asegurar que la API responde correctamente

  Background:
    Given el actor puede consumir la API de Todos

  # ── GET ───────────────────────────────────────────────────────────────────

  @smoke @regression
  Scenario: Obtener un todo existente por ID
    When obtiene el todo con ID 1
    Then el status code de la respuesta es 200
    And el todo tiene ID 1 y userId 1
    And el todo tiene un título no nulo
    And el campo completed es de tipo booleano

  @negative
  Scenario: Obtener un todo con ID inexistente devuelve 404
    When obtiene el todo con ID 99999
    Then el status code de la respuesta es 404
