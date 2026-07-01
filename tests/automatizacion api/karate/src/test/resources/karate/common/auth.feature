# common/auth.feature
# ─────────────────────────────────────────────────────────────────────────────
# Helper de autenticación reutilizable.
# Marcado @ignore para que no se ejecute directamente.
# Llamar con: * def authResult = call read('classpath:karate/common/auth.feature') credentials
#
# Variables de entrada: email, password
# Variables de salida:  token (string)
# ─────────────────────────────────────────────────────────────────────────────

@ignore
Feature: Helper de autenticación — ReqRes API

  Scenario: Obtener Bearer Token
    Given url reqresUrl + '/login'
    And headers reqresHeaders
    And request { email: '#(email)', password: '#(password)' }
    When method POST
    Then status 200
    And match response.token == '#string'
    * def token = response.token
