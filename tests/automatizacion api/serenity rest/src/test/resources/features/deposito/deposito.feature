# language: es
@tx02 @deposito
Característica: TX-02 Depósitos y consignaciones en efectivo
  Como sistema ATM del Banco de Bogotá
  Quiero procesar solicitudes de depósito y consignación en efectivo
  Para que los clientes puedan depositar dinero desde cajeros automáticos

  Antecedentes:
    Dado el actor está autorizado para operar en la API de depósitos

  @smoke @e2e
  Esquema del escenario: Depósito en efectivo - respuesta exitosa
    Cuando realiza un depósito en efectivo del caso <Caso>
    Entonces la transacción de depósito es exitosa con código "200"
    Y el campo endDt del depósito está presente

    Ejemplos:
      ##@externaldata@src/test/resources/datadriven/datadriven.xlsx@deposito
      | Caso |
      |1|
      |2|

  @e2e @validacion-estado
  Esquema del escenario: Depósito en efectivo - severidad Info confirmada
    Cuando realiza un depósito en efectivo del caso <Caso>
    Entonces la severidad del depósito es "Info"

    Ejemplos:
      ##@externaldata@src/test/resources/datadriven/datadriven.xlsx@deposito
      | Caso |
      |1|
      |2|

  @e2e @validacion-mensaje
  Esquema del escenario: Depósito en efectivo - descripción transacción exitosa
    Cuando realiza un depósito en efectivo del caso <Caso>
    Entonces la descripción del depósito es "Transaccion exitosa"

    Ejemplos:
      ##@externaldata@src/test/resources/datadriven/datadriven.xlsx@deposito
      | Caso |
      |1|
      |2|

  @e2e @flujo-completo
  Esquema del escenario: Depósito en efectivo - validación completa de respuesta
    Cuando realiza un depósito en efectivo del caso <Caso>
    Entonces la transacción de depósito es exitosa con código "200"
    Y la severidad del depósito es "Info"
    Y la descripción del depósito es "Transaccion exitosa"
    Y el campo endDt del depósito está presente

    Ejemplos:
      ##@externaldata@src/test/resources/datadriven/datadriven.xlsx@deposito
      | Caso |
      |1|
      |2|

