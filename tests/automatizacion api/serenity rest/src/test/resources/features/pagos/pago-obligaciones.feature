# language: es
@tx04 @pago-obligaciones
Característica: TX-04 Pago de obligaciones y TC Aval en efectivo
  Como sistema ATM del Banco de Bogotá
  Quiero procesar pagos de obligaciones y Tarjeta de Crédito Aval
  Para que los clientes puedan pagar sus obligaciones financieras desde cajeros automáticos

  Antecedentes:
    Dado el actor está autorizado para operar en la API de pago de obligaciones

  @smoke @e2e
  Escenario: TX-04 Pago de obligación TC Aval - respuesta exitosa HTTP 200
    Cuando realiza el pago de la obligación TC Aval
    Entonces el pago de obligación es exitoso con HTTP 200
