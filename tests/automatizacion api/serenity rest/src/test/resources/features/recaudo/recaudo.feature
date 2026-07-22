# language: es
@tx03 @recaudo
Característica: TX-03 Recaudo de convenios en efectivo
  Como sistema ATM del Banco de Bogotá
  Quiero procesar pagos de convenios en dos pasos (consulta + pago)
  Para que los clientes puedan pagar facturas de servicios desde cajeros automáticos

  Antecedentes:
    Dado el actor está autorizado para operar en la API de recaudo

  @smoke @e2e @paso1
  Escenario: TX-03 Paso 1 - Consulta de factura de convenio exitosa
    Cuando consulta la factura del convenio TX-03
    Entonces la consulta de factura es exitosa con código "200"

  @e2e @paso1 @validacion-datos
  Escenario: TX-03 Paso 1 - Consulta retorna nombre del convenio
    Cuando consulta la factura del convenio TX-03
    Entonces la consulta de factura es exitosa con código "200"
    Y la respuesta contiene el nombre del convenio

  @e2e @paso1 @validacion-datos
  Escenario: TX-03 Paso 1 - Consulta retorna monto total y saldos
    Cuando consulta la factura del convenio TX-03
    Entonces la consulta de factura es exitosa con código "200"
    Y la respuesta contiene el monto total a pagar
    Y los saldos de la factura están presentes

  @smoke @e2e @paso2
  Escenario: TX-03 Paso 2 - Pago de factura de convenio exitoso
    Cuando realiza el pago de la factura del convenio
    Entonces el pago de la factura es exitoso con código "200"
    Y el campo endDt del recaudo está presente

  @e2e @paso2 @validacion-estado
  Escenario: TX-03 Paso 2 - Pago de factura con severidad Info
    Cuando realiza el pago de la factura del convenio
    Entonces la severidad del recaudo es "Info"
    Y la descripción del recaudo es "Transaccion exitosa"

  @e2e @flujo-completo
  Escenario: TX-03 Flujo completo - Consulta y pago de convenio
    Cuando consulta la factura del convenio TX-03
    Entonces la consulta de factura es exitosa con código "200"
    Y la respuesta contiene el nombre del convenio
    Y la respuesta contiene el monto total a pagar
    Cuando realiza el pago de la factura del convenio
    Entonces el pago de la factura es exitoso con código "200"
    Y la severidad del recaudo es "Info"
    Y la descripción del recaudo es "Transaccion exitosa"
    Y el campo endDt del recaudo está presente

  @e2e @no-happy-path @outline
  Esquema del escenario: TX-03 No happy path - consulta con trnRqUID MOCK-x
    Cuando consulta la factura del convenio TX-03 con trnRqUID "<trnRqUID>"
    Entonces la consulta no happy path retorna status code "<statusCode>" y estado corporativo "<estadoCorporativo>"

    Ejemplos:
      | trnRqUID | statusCode | estadoCorporativo |
      | MOCK-204 | 204        | REVERSADA         |
      | MOCK-100 | 100        | FALLIDA_NEGOCIO   |
      | MOCK-300 | 300        | FALLIDA_TECNICA   |
      | MOCK-600 | 600        | FALLIDA_ENTIDAD   |
      | MOCK-700 | 700        | FALLIDA_GENERAL   |
      | MOCK-900 | 900        | PENDIENTE         |
      | MOCK-901 | 901        | TIMEOUT           |
