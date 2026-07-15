# language: es
@tx01 @retiro
Característica: TX-01 Retiro de efectivo con OTP
  Como sistema ATM del Banco de Bogotá
  Quiero procesar solicitudes de retiro de efectivo con OTP
  Para que los clientes puedan retirar dinero de forma segura desde cajeros automáticos

  Antecedentes:
    Dado el actor está autorizado para operar en la API de retiros

  @smoke @e2e
  Escenario: Retiro de efectivo con OTP - respuesta exitosa
    Cuando realiza un retiro de efectivo con OTP
    Entonces la transacción de retiro es exitosa con código "200"
    Y el campo endDt del retiro está presente

  @e2e @validacion-estado
  Escenario: Retiro de efectivo - severidad Info confirmada
    Cuando realiza un retiro de efectivo con OTP
    Entonces la severidad del retiro es "Info"

  @e2e @validacion-mensaje
  Escenario: Retiro de efectivo - descripción transacción exitosa
    Cuando realiza un retiro de efectivo con OTP
    Entonces la descripción del retiro es "Transaccion exitosa"

  @e2e @flujo-completo
  Escenario: Retiro de efectivo - validación completa de respuesta
    Cuando realiza un retiro de efectivo con OTP
    Entonces la transacción de retiro es exitosa con código "200"
    Y la severidad del retiro es "Info"
    Y la descripción del retiro es "Transaccion exitosa"
    Y el campo endDt del retiro está presente
