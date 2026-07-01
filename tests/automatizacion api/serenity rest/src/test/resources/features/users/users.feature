@users
Feature: Gestion de Usuarios - JSONPlaceholder API

  Como equipo de QA
  Quiero validar los endpoints del recurso Users
  Para asegurar que la API responde correctamente

  Background:
    Given el actor puede consumir la API de JSONPlaceholder para usuarios

  @smoke @regression
  Scenario: Obtener usuario existente por ID
    When obtiene el usuario con ID 1
    Then el status code del usuario es 200
    And el usuario tiene ID 1
    And el usuario tiene email no nulo

  @negative
  Scenario: Obtener usuario con ID inexistente devuelve 404
    When obtiene el usuario con ID 99999
    Then el status code del usuario es 404