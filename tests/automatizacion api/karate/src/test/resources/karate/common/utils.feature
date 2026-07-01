# common/utils.feature
# ─────────────────────────────────────────────────────────────────────────────
# Utilidades reutilizables entre features.
# Marcado @ignore — no se ejecuta directamente.
# ─────────────────────────────────────────────────────────────────────────────

@ignore
Feature: Utilidades comunes

  # Genera un timestamp único para usar como identificador en POSTs
  # Uso: * def uniqueId = call read('classpath:karate/common/utils.feature@generateId')
  @generateId
  Scenario: Generar identificador único basado en timestamp
    * def uniqueId = 'demo-' + java.lang.System.currentTimeMillis()

  # Pausa configurable (ms) — usar solo cuando la API tiene rate-limit
  # Uso: * call read('classpath:karate/common/utils.feature@pause') { ms: 500 }
  @pause
  Scenario: Esperar N milisegundos
    * pause(ms)
