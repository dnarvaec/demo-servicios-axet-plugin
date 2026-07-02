@ecopetrol @e2e
Feature: Ciclo de Vida E2E — App Central Ecopetrol

  Como equipo de QA
  Quiero validar el flujo completo de autenticación y navegación en App Central Ecopetrol
  Para asegurar que el ciclo de vida end-to-end funciona correctamente de extremo a extremo

  Background:
    Given el actor inicia el navegador con Playwright

  @smoke @regression
  Scenario: Login exitoso y selección de rol Líder
    When el actor ingresa usuario "jtorres" y contraseña "abc123"
    And hace clic en "Iniciar sesión"
    Then debe ver la pantalla de bienvenida con los roles disponibles
    And el rol "Líder" está disponible y puede ser seleccionado
    When selecciona el rol Líder
    Then accede al dashboard correspondiente al rol Líder

  @negative
  Scenario: Login con credenciales incorrectas muestra error
    When el actor ingresa usuario "usuario_invalido" y contraseña "pass_incorrecta"
    And hace clic en "Iniciar sesión"
    Then la pantalla de login permanece visible o muestra un mensaje de error
