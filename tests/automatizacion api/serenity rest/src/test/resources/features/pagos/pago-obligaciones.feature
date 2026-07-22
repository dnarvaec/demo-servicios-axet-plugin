# language: es
@tx04 @pago-obligaciones
Característica: TX-04 Pago de obligaciones y TC Aval en efectivo
  Como sistema ATM del Banco de Bogotá
  Quiero procesar pagos de obligaciones y Tarjeta de Crédito Aval
  Para que los clientes puedan pagar sus obligaciones financieras desde cajeros automáticos

  Antecedentes:
    Dado el actor está autorizado para operar en la API de pago de obligaciones

  @smoke @e2e
  Escenario: TX-04 Pago de obligación TC Aval - respuesta exitosa
    Cuando realiza el pago de la obligación TC Aval
    Entonces la transacción de pago de obligación es exitosa con código "200"
    Y el campo endDt del pago de obligación está presente

  @e2e @validacion-estado
  Escenario: TX-04 Pago de obligación TC Aval - severidad Info confirmada
    Cuando realiza el pago de la obligación TC Aval
    Entonces la severidad del pago de obligación es "Info"

  @e2e @validacion-mensaje
  Escenario: TX-04 Pago de obligación TC Aval - descripción transacción exitosa
    Cuando realiza el pago de la obligación TC Aval
    Entonces la descripción del pago de obligación es "Transaccion exitosa"

  @e2e @flujo-completo
  Escenario: TX-04 Pago de obligación TC Aval - validación completa de respuesta
    Cuando realiza el pago de la obligación TC Aval
    Entonces la transacción de pago de obligación es exitosa con código "200"
    Y la severidad del pago de obligación es "Info"
    Y la descripción del pago de obligación es "Transaccion exitosa"
    Y el campo endDt del pago de obligación está presente
