# Análisis Técnico — Colección Postman "Everest AVC"

> **Fecha de análisis:** 2026-07-23  
> **Colección:** Everest AVC (`_postman_id`: 0a81c3f3-0d5f-4501-9bce-7080baefa8f7)  
> **Schema:** Postman Collection v2.1.0  

---

## 1. Resumen General

| # | Operación | Endpoint | Método |
|---|-----------|----------|--------|
| 1 | RETIRO | `/api/v1/pagos/retiro` | POST |
| 2 | DEPOSITO | `/api/v1/pagos/deposito` | POST |
| 3 | CONSULTA_FACTURA | `/everest/orq/consultas/api/v1/consulta` | POST |
| 4 | PAGO_FACTURA | `/api/v1/pagos/pago-factura` | POST |
| 5 | PAGO_OBLIGACIONES | `/api/v1/pagos/pago-obligaciones` | POST |

- **Base URL:** `https://d2q3sea1wnkwiy.cloudfront.net` (AWS CloudFront)
- **Total de requests:** 5
- **Protocolo:** HTTPS
- **Formato de datos:** JSON (todas las peticiones)

---

## 2. Análisis por Endpoint

---

### 2.1 RETIRO

**URL:** `https://d2q3sea1wnkwiy.cloudfront.net/api/v1/pagos/retiro`  
**Método:** POST

#### Headers
| Header | Valor | Tipo |
|--------|-------|------|
| Content-Type | application/json | Fijo |
| Authorization | Bearer `{{bearer_token_0gro}}` | Variable de entorno |
| X-Transaction-Id | 1234567890 | Valor de prueba |
| X-RqUID | abcde-12345-fghij-67890 | Valor de prueba |
| X-Channel | ATM | Canal cajero automático |
| X-CompanyId | BANCO_BOGOTA | Identificador del banco |
| X-IPAddr | 192.168.0.10 | IP del dispositivo |
| X-IP-client | 192.168.0.10 | IP del cliente |
| X-NextDt | 2026-07-14T11:31:00-05:00 | Fecha/hora (UTC-5) |
| X-ClientDt | 2026-07-14T11:31:00-05:00 | Fecha/hora cliente |
| X-CustIdentType | CC | Tipo de identificación |
| X-CustIdentNum | 123456789 | Número de identificación |
| X-SessKey | session-key-xyz | Clave de sesión |
| X-Language | ES | Idioma |
| X-CustLoginId | user-login-id | ID de login del usuario |
| X-IBM-Client-Id | ccc7806154afefbbe6a3c1c2a2ffb8e8 | Client ID IBM API Connect |
| X-Device-ID | ATM-BOG-138 | ID del dispositivo (ATM Bogotá) |

#### Estructura del Body
| Campo | Valor ejemplo | Descripción |
|-------|--------------|-------------|
| `banco` | `BANCO_BOGOTA` | Identificador del banco |
| `operacion` | `RETIRO` | Tipo de operación |
| `operacionobj.NetworkTrnInfo.OriginatorName` | `BMOB` | Nombre del originador |
| `operacionobj.NetworkTrnInfo.OriginatorType` | `021` | Tipo originador (ATM) |
| `operacionobj.NetworkTrnInfo.TerminalId` | `00BOG138` | ID del terminal ATM |
| `operacionobj.NetworkTrnInfo.NetworkRefId` | `7946` | Referencia de red |
| `operacionobj.NetworkTrnInfo.TeminalSequence` | `6032` | Secuencia del terminal |
| `operacionobj.NetworkTrnInfo.IncocredCode` | `457896` | Código Incocred |
| `operacionobj.NetworkTrnInfo.PostAddr.Addr1` | `Direccion` | Dirección del ATM |
| `operacionobj.NetworkTrnInfo.PostAddr.StateProv` | `2302` | Código departamento/ciudad |
| `operacionobj.PartyAcctRelInfo.DepAcctIdFrom.DepAcctId.AcctType` | `DDA` | Tipo de cuenta origen |
| `operacionobj.PartyAcctRelInfo.DepAcctIdFrom.DepAcctId.AcctKey` | `4915110205551818=23121011339517000000` | Track 2 de la tarjeta |
| `operacionobj.PartyAcctRelInfo.CardAcctIdFrom.CardAcctId.AcctId` | `4915110205551818` | PAN de la tarjeta |
| `operacionobj.DepAcctId.AcctType` | `DDA` | Tipo cuenta destino |
| `operacionobj.DepAcctId.BankInfo.BankId` | `00010016` | ID del banco |
| `operacionobj.ContactInfo.PhoneNum.PhoneType` | `Celular` | Tipo teléfono |
| `operacionobj.ContactInfo.PhoneNum.Phone` | `3118451263` | Número de teléfono |
| `operacionobj.CurAmt.Amt` | `20000.00` | **Monto del retiro: $20,000 COP** |
| `operacionobj.CurAmt.CurCode` | `COP` | Código de moneda ISO 4217 |
| `operacionobj.Fee.CurAmt.Amt` | `1800.00` | **Comisión: $1,800 COP** |
| `operacionobj.Fee.CurAmt.CurCode` | `COP` | Moneda de la comisión |
| `operacionobj.OTPInfo.OtpType` | `OTP` | Tipo de OTP |
| `operacionobj.OTPInfo.OtpValue` | `1245` | Valor OTP (exclusivo RETIRO) |

---

### 2.2 DEPOSITO

**URL:** `https://d2q3sea1wnkwiy.cloudfront.net/api/v1/pagos/deposito`  
**Método:** POST

#### Headers
Idénticos a RETIRO, excepto:
- `X-NextDt` / `X-ClientDt`: `2026-07-14T11:34:00-05:00` (3 minutos después que RETIRO)

#### Estructura del Body — Diferencias respecto a RETIRO
| Campo | RETIRO | DEPOSITO | Diferencia |
|-------|--------|----------|------------|
| `operacion` | `RETIRO` | `DEPOSITO` | Tipo de operación |
| `NetworkTrnInfo.OriginatorType` | `021` | `010` | Código de tipo originador |
| `NetworkTrnInfo.NetworkRefId` | `7946` | `7948` | Referencia de red distinta |
| `NetworkTrnInfo.TeminalSequence` | `6032` | `603237` | Secuencia distinta |
| `NetworkTrnInfo.IncocredCode` | `457896` | `184990` | Código Incocred distinto |
| `NetworkTrnInfo.PostAddr.Addr1` | `Direccion` | `Direccion 43` | Dirección diferente |
| `PartyAcctRelInfo...AcctKey` | `4915110205551818=...` | `4473990520002999=...` | Tarjeta origen diferente |
| `CardAcctIdFrom.AcctId` | `4915110205551818` | `4473990520002999` | PAN diferente |
| `DepAcctId.AcctId` | ❌ Ausente | `0015423459` | Cuenta destino explícita |
| `OTPInfo` | ✅ Presente | ❌ Ausente | DEPOSITO no requiere OTP |

---

### 2.3 CONSULTA_FACTURA

**URL:** `https://d2q3sea1wnkwiy.cloudfront.net/everest/orq/consultas/api/v1/consulta`  
**Método:** POST

> ⚠️ **ATENCIÓN — URL diferente:** Único endpoint con path `/everest/orq/consultas/...`  
> El resto de operaciones usan `/api/v1/pagos/...`

#### Headers
| Header | Valor | Diferencias respecto a RETIRO/DEPOSITO |
|--------|-------|---------------------------------------|
| Authorization | `Bearer x` | ⚠️ Token hardcodeado, no variable de entorno |
| X-Channel | `CBV` | Canal distinto (no ATM) |
| X-CompanyId | `00010016` | Formato numérico (RETIRO usa `BANCO_BOGOTA`) |
| X-RqUID | `1667669` | Formato numérico simplificado |
| X-CustIdentNum | `10123377654` | Número de cédula diferente |
| X-Device-ID | `TEST-DEVICE-001` | Nombre de dispositivo de prueba |
| X-Transaction-Id | ❌ Ausente | No se envía |
| X-SessKey | ❌ Ausente | No se envía |
| X-Language | ❌ Ausente | No se envía |
| X-CustLoginId | ❌ Ausente | No se envía |

#### Estructura del Body
| Campo | Valor | Nota |
|-------|-------|------|
| `banco` | `bbogota` | ⚠️ En minúsculas (otros usan `BANCO_BOGOTA`) |
| `operacion` | `CONSULTA_FACTURA` | |
| `obj_operacion` | objeto | ⚠️ Clave `obj_operacion` (otros usan `operacionobj`) |
| `obj_operacion.NetwokInfo.NetworkOwner` | `7946` | ⚠️ Typo en campo: `NetwokInfo` (falta 'r') |
| `obj_operacion.NetwokInfo.NetworkRefId` | `7946` | |
| `obj_operacion.Transaction.TrnRqUID` | `MOCK100` | Prefijo MOCK indica dato de prueba |
| `obj_operacion.Transaction.TrnSrc` | `BMOB` | |
| `obj_operacion.Transaction.TerminalSequence` | `4594971` | Nombre diferente: `TerminalSequence` vs `TeminalSequence` en otros |
| `obj_operacion.Agreement.AgrmId` | `7946` | ID del convenio |
| `obj_operacion.Agreement.InvoiceNum` | `123456789` | Número de factura |
| `obj_operacion.Agreement.ExpDt` | `2020-06-24T16:05:45.314` | ⚠️ Fecha vencida (año 2020) |
| `obj_operacion.Agreement.CSPRefId` | `12` | ID referencia CSP |
| `obj_operacion.Agreement.DepAcctId.AcctId` | `*****4207` | Número de cuenta enmascarado |
| `obj_operacion.Agreement.DepAcctId.AcctType` | `CCA` | Tipo cuenta (Credit Card Account) |
| `obj_operacion.InvoiceSender.AcctPayAcct` | `1003214830` | Cuenta pagadora de factura |
| `obj_operacion.InvoiceSender.SvcId` | `0007` | ID del servicio |
| `obj_operacion.InvoiceSender.InvSndrPmtInfo.POSEntryMode` | `010` | Modo de entrada POS |
| `obj_operacion.InvoiceSender.AgrmType` | `1` | Tipo de convenio |
| `obj_operacion.PSPCity.CityId` | `11001` | Código DANE de Bogotá |
| `obj_operacion.LocationInfo.GeoLocation` | `KR 11 # 71 -73` | Dirección física |

---

### 2.4 PAGO_FACTURA

**URL:** `https://d2q3sea1wnkwiy.cloudfront.net/api/v1/pagos/pago-factura`  
**Método:** POST

> ⚠️ **ATENCIÓN — Headers con datos inválidos:** Varios headers contienen valores que no son válidos para un ambiente real

#### Headers
| Header | Valor | Problema |
|--------|-------|---------|
| Authorization | `Bearer x` | ⚠️ Token hardcodeado |
| X-Transaction-Id | `78789` | ✅ Válido |
| X-RqUID | `09898` | ✅ Válido |
| X-Channel | `89098789` | ⚠️ Valor numérico inválido para canal |
| X-CompanyId | `789890` | ⚠️ Valor numérico inválido |
| X-IPAddr | `677678` | ⚠️ No es una dirección IP válida |
| X-NextDt | `6756789` | ⚠️ No es una fecha válida |
| X-IP-client | `192.168.1.100` | ✅ Válido |
| X-Device-ID | `TEST-DEVICE-001` | ✅ Válido (igual a CONSULTA_FACTURA) |
| X-IBM-Client-Id | `ccc7806154afefbbe6a3c1c2a2ffb8e8` | ✅ Igual al resto |
| X-ClientDt | ❌ Ausente | No se envía |
| X-CustIdentType | ❌ Ausente | No se envía |
| X-CustIdentNum | ❌ Ausente | No se envía |
| X-SessKey | ❌ Ausente | No se envía |
| X-Language | ❌ Ausente | No se envía |
| X-CustLoginId | ❌ Ausente | No se envía |

#### Estructura del Body
| Campo | Valor | Nota |
|-------|-------|------|
| `banco` | `BANCO_BOGOTA` | ✅ Consistente con RETIRO/DEPOSITO |
| `operacion` | `PAGO_FACTURA` | |
| `operacionobj` | objeto | ✅ Clave consistente con RETIRO/DEPOSITO |
| `operacionobj.NetwokInfo.NetworkRefId` | `7946` | ⚠️ Mismo typo `NetwokInfo` que CONSULTA_FACTURA |
| `operacionobj.NetwokInfo.NetworkOwner` | `7946` | |
| `operacionobj.Transaction.TrnRqUID` | `7946` | |
| `operacionobj.Transaction.TrnSrc` | `BMOB` | |
| `operacionobj.Transaction.TerminalSequence` | `4595976` | |
| `operacionobj.Transaction.RefInfo[0].RefId` | `10002795130011` | Referencia de pago |
| `operacionobj.Transaction.RefInfo[0].RefType` | `Referencia3` | Tipo de referencia |
| `operacionobj.TotalCurAmt.Amt` | `10000.00` | Monto total a pagar |
| `operacionobj.TotalCurAmt.CurCode` | `170` | ⚠️ Código numérico, no ISO (otros usan `COP`) |
| `operacionobj.Agreement.NIE` | `12053337612` | Número Interno de Empresa |
| `operacionobj.Agreement.AgrmId` | `7946` | ID del convenio |
| `operacionobj.Agreement.InvoiceNum` | `4915110205551818=23121011339517000000` | Número de factura (Track 2) |
| `operacionobj.Agreement.ExpDt` | `2020-06-24T20:21:03.314` | ⚠️ Fecha vencida (año 2020) |
| `operacionobj.Agreement.DepAcctId.AcctId` | `821004207` | Cuenta destino |
| `operacionobj.Agreement.DepAcctId.AcctType` | `CCA` | Tipo cuenta (Credit Card Account) |
| `operacionobj.InvoiceSender.AcctPayAcct` | `12053337612` | Cuenta pagadora |
| `operacionobj.InvoiceSender.InvSndrPmtInfo.POSEntryMode` | `010` | Modo entrada POS |
| `operacionobj.InvoiceSender.SvcId` | `00007` | ID servicio (5 dígitos, CONSULTA usa `0007`) |
| `operacionobj.PSPCity.CityId` | `90025` | Código ciudad |
| `operacionobj.LocationInfo.GeoLocation` | `KR 11 # 71 -73` | Dirección física |
| `operacionobj.AcctBal[0].Desc` | `ValorPrincipal` | Descripción del balance |
| `operacionobj.AcctBal[0].CurAmt.Amt` | `38020.00` | Valor principal |
| `operacionobj.AcctBal[1].Desc` | `ValorAdicional1` | Cargo adicional |
| `operacionobj.AcctBal[1].CurAmt.Amt` | `16520.00` | Valor adicional |

---

### 2.5 PAGO_OBLIGACIONES

**URL:** `https://d2q3sea1wnkwiy.cloudfront.net/api/v1/pagos/pago-obligaciones`  
**Método:** POST

#### Headers
| Header | Valor | Nota |
|--------|-------|------|
| Authorization | Bearer `{{bearer_token_0gro}}` | ✅ Variable de entorno |
| X-Transaction-Id | `78789` | |
| X-RqUID | `09898` | |
| X-Channel | `89098789` | ⚠️ Valor numérico inválido para canal |
| X-CompanyId | `789890` | ⚠️ Valor numérico inválido |
| X-IPAddr | `192.168.1.100` | ✅ IP válida |
| X-IP-client | `192.168.1.100` | ✅ IP válida |
| X-NextDt | `2026-07-22T12:00:00-05:00` | ✅ Fecha válida |
| X-ClientDt | `2026-07-22T12:00:00-05:00` | ✅ Fecha válida |
| X-IdentSerialNum | `asd` | ⚠️ Valor de prueba no representativo |
| X-GovIssueIdentType | `qwe` | ⚠️ Valor de prueba no representativo |
| X-IBM-Client-Id | `ccc7806154afefbbe6a3c1c2a2ffb8e8` | ✅ Consistente |
| X-Device-ID | `TEST-001` | Dispositivo de prueba |
| X-CustIdentType | ❌ Ausente | |
| X-CustIdentNum | ❌ Ausente | |

#### Estructura del Body
| Campo | Valor | Nota |
|-------|-------|------|
| `banco` | `BANCO_BOGOTA` | ✅ Consistente |
| `operacion` | `PAGO_OBLIGACIONES` | |
| `operacionobj.NetworkTrnInfo.OriginatorName` | `BMOB` | ✅ Igual a RETIRO/DEPOSITO |
| `operacionobj.NetworkTrnInfo.OriginatorType` | `021` | Mismo que RETIRO |
| `operacionobj.NetworkTrnInfo.TerminalId` | `00BOG138` | ID del terminal |
| `operacionobj.NetworkTrnInfo.NetworkRefId` | `7946` | |
| `operacionobj.NetworkTrnInfo.TeminalSequence` | `6032` | |
| `operacionobj.NetworkTrnInfo.PostAddr.Addr1` | `Direccion` | |
| `operacionobj.NetworkTrnInfo.PostAddr.StateProv` | `2302` | |
| `operacionobj.LoanPmtInfo.DepAcctIdFrom.DepAcctId.AcctType` | `DDA` | Cuenta origen |
| `operacionobj.LoanPmtInfo.DepAcctIdFrom.DepAcctId.AcctKey` | `4915110205551818=23121011339517000000` | Track 2 |
| `operacionobj.LoanPmtInfo.DepAcctIdTo.DepAcctId.AcctId` | `200000100000900591657949` | Cuenta destino (obligación) |
| `operacionobj.LoanPmtInfo.DepAcctIdTo.DepAcctId.BankInfo.BankId` | `00010016` | Banco destino |
| `operacionobj.LoanPmtInfo.CurAmt.Amt` | `20000.00` | **Monto: $20,000 COP** |
| `operacionobj.LoanPmtInfo.CurAmt.CurCode` | `COP` | ✅ ISO 4217 |
| `operacionobj.LoanPmtInfo.LoanPmtType` | `CCA` | Tipo de pago de obligación |
| `operacionobj.LoanPmtInfo.LoanPmtComplement` | `7946` | Complemento del pago |
| `operacionobj.LoanPmtInfo.CardAcctIdFrom.CardAcctId.AcctId` | `4915110205551818` | PAN de la tarjeta origen |

---

## 3. Matriz Comparativa de Headers

| Header | RETIRO | DEPOSITO | CONSULTA_FACTURA | PAGO_FACTURA | PAGO_OBLIGACIONES |
|--------|:------:|:--------:|:----------------:|:------------:|:-----------------:|
| Content-Type | ✅ | ✅ | ✅ | ✅ | ✅ |
| Authorization (variable env) | ✅ | ✅ | ❌ hardcoded | ❌ hardcoded | ✅ |
| X-Transaction-Id | ✅ | ✅ | ❌ | ✅ | ✅ |
| X-RqUID | ✅ | ✅ | ✅ | ✅ | ✅ |
| X-Channel | ATM | ATM | CBV | ⚠️ inválido | ⚠️ inválido |
| X-CompanyId | BANCO_BOGOTA | BANCO_BOGOTA | 00010016 | ⚠️ inválido | ⚠️ inválido |
| X-IPAddr | ✅ | ✅ | ✅ | ⚠️ inválido | ✅ |
| X-IP-client | ✅ | ✅ | ✅ | ✅ | ✅ |
| X-NextDt | ✅ | ✅ | ✅ | ⚠️ inválido | ✅ |
| X-ClientDt | ✅ | ✅ | ✅ | ❌ | ✅ |
| X-CustIdentType | ✅ | ✅ | ✅ | ❌ | ❌ |
| X-CustIdentNum | ✅ | ✅ | ✅ | ❌ | ❌ |
| X-SessKey | ✅ | ✅ | ❌ | ❌ | ❌ |
| X-Language | ✅ | ✅ | ❌ | ❌ | ❌ |
| X-CustLoginId | ✅ | ✅ | ❌ | ❌ | ❌ |
| X-IBM-Client-Id | ✅ | ✅ | ✅ | ✅ | ✅ |
| X-Device-ID | ✅ | ✅ | ✅ | ✅ | ✅ |
| X-IdentSerialNum | ❌ | ❌ | ❌ | ❌ | ✅ |
| X-GovIssueIdentType | ❌ | ❌ | ❌ | ❌ | ✅ |

---

## 4. Matriz Comparativa de Body

| Campo / Objeto | RETIRO | DEPOSITO | CONSULTA_FACTURA | PAGO_FACTURA | PAGO_OBLIGACIONES |
|----------------|:------:|:--------:|:----------------:|:------------:|:-----------------:|
| `banco` | `BANCO_BOGOTA` | `BANCO_BOGOTA` | `bbogota` ⚠️ | `BANCO_BOGOTA` | `BANCO_BOGOTA` |
| Clave objeto principal | `operacionobj` | `operacionobj` | `obj_operacion` ⚠️ | `operacionobj` | `operacionobj` |
| Bloque de red | `NetworkTrnInfo` | `NetworkTrnInfo` | `NetwokInfo` ⚠️ | `NetwokInfo` ⚠️ | `NetworkTrnInfo` |
| Información de cuenta | `PartyAcctRelInfo` | `PartyAcctRelInfo` | — | — | `LoanPmtInfo` |
| Bloque de transacción | — | — | `Transaction` | `Transaction` | — |
| `Agreement` | — | — | ✅ | ✅ | — |
| `InvoiceSender` | — | — | ✅ | ✅ | — |
| `OTPInfo` | ✅ | ❌ | ❌ | ❌ | ❌ |
| `CurAmt` directo | ✅ | ✅ | — | `TotalCurAmt` | — |
| `CurAmt` en `LoanPmtInfo` | — | — | — | — | ✅ |
| `Fee` | ✅ | ✅ | — | — | — |
| `AcctBal` (array) | — | — | — | ✅ | — |
| `ContactInfo` | ✅ | ✅ | — | — | — |
| `PSPCity` | — | — | ✅ | ✅ | — |
| `LocationInfo` | — | — | ✅ | ✅ | — |
| Moneda (`CurCode`) | `COP` | `COP` | — | `170` ⚠️ | `COP` |

---

## 5. Inconsistencias y Anomalías Detectadas

### 5.1 Inconsistencias de Estructura

| ID | Descripción | Afecta |
|----|-------------|--------|
| I-01 | Clave del objeto de operación varía: `operacionobj` vs `obj_operacion` | CONSULTA_FACTURA vs el resto |
| I-02 | Nombre del bloque de red: `NetworkTrnInfo` vs `NetwokInfo` (typo, falta 'r' en "Network") | CONSULTA_FACTURA y PAGO_FACTURA |
| I-03 | Nombre del campo de secuencia: `TeminalSequence` (typo) vs `TerminalSequence` | RETIRO/DEPOSITO/PAGO_OBLIGACIONES vs CONSULTA_FACTURA/PAGO_FACTURA |
| I-04 | Campo `banco` en minúsculas `bbogota` en CONSULTA_FACTURA vs `BANCO_BOGOTA` en el resto | CONSULTA_FACTURA |
| I-05 | URL de CONSULTA_FACTURA usa path completamente diferente al resto de operaciones | CONSULTA_FACTURA |
| I-06 | Código de moneda `170` (numérico) en PAGO_FACTURA vs `COP` (ISO 4217) en el resto | PAGO_FACTURA |
| I-07 | `SvcId` en CONSULTA_FACTURA es `0007` (4 dígitos), en PAGO_FACTURA es `00007` (5 dígitos) | CONSULTA_FACTURA / PAGO_FACTURA |
| I-08 | `DepAcctId.AcctId` ausente en RETIRO pero presente en DEPOSITO (mismo objeto) | RETIRO |

### 5.2 Problemas de Autenticación

| ID | Descripción | Afecta |
|----|-------------|--------|
| A-01 | `Authorization: Bearer x` hardcodeado (token vacío/inválido) | CONSULTA_FACTURA, PAGO_FACTURA |
| A-02 | Variable de entorno `{{bearer_token_0gro}}` solo aplicada en RETIRO, DEPOSITO y PAGO_OBLIGACIONES | CONSULTA_FACTURA, PAGO_FACTURA |

### 5.3 Headers con Valores Inválidos

| ID | Header | Valor inválido | Operación |
|----|--------|---------------|-----------|
| H-01 | X-Channel | `89098789` | PAGO_FACTURA, PAGO_OBLIGACIONES |
| H-02 | X-CompanyId | `789890` | PAGO_FACTURA, PAGO_OBLIGACIONES |
| H-03 | X-IPAddr | `677678` | PAGO_FACTURA |
| H-04 | X-NextDt | `6756789` | PAGO_FACTURA |
| H-05 | X-IdentSerialNum | `asd` (placeholder) | PAGO_OBLIGACIONES |
| H-06 | X-GovIssueIdentType | `qwe` (placeholder) | PAGO_OBLIGACIONES |

### 5.4 Fechas Vencidas en Datos de Prueba

| ID | Campo | Valor | Operación |
|----|-------|-------|-----------|
| F-01 | `Agreement.ExpDt` | `2020-06-24T16:05:45.314` | CONSULTA_FACTURA |
| F-02 | `Agreement.ExpDt` | `2020-06-24T20:21:03.314` | PAGO_FACTURA |

---

## 6. Dominio de Negocio — Interpretación Funcional

### 6.1 Flujo de Operaciones ATM (Canal ATM)

Las operaciones **RETIRO**, **DEPOSITO** y **PAGO_OBLIGACIONES** comparten el mismo canal `ATM` y estructura `NetworkTrnInfo`, lo que indica que son transacciones procesadas directamente en cajeros automáticos del Banco de Bogotá.

```
[Cliente ATM]
    │
    ├─► RETIRO          → /api/v1/pagos/retiro          (requiere OTP)
    ├─► DEPOSITO        → /api/v1/pagos/deposito         (sin OTP, cuenta destino explícita)
    └─► PAGO_OBLIGACIONES → /api/v1/pagos/pago-obligaciones (pago de créditos/préstamos)
```

### 6.2 Flujo de Recaudo de Facturas (Canal CBV)

Las operaciones **CONSULTA_FACTURA** y **PAGO_FACTURA** conforman un flujo de dos pasos para el pago de servicios públicos o facturas a través de un canal de recaudo (CBV = Cobros y Validaciones).

```
[Canal CBV / POS]
    │
    ├─► PASO 1: CONSULTA_FACTURA → /everest/orq/consultas/api/v1/consulta
    │           (Verifica existencia y estado de la factura)
    │
    └─► PASO 2: PAGO_FACTURA    → /api/v1/pagos/pago-factura
                (Ejecuta el pago con los datos obtenidos en la consulta)
```

### 6.3 Significado de Campos Clave

| Campo | Significado |
|-------|-------------|
| `AcctType: DDA` | Demand Deposit Account (Cuenta de Ahorros o Corriente) |
| `AcctType: CCA` | Credit Card Account (Cuenta de Tarjeta de Crédito) |
| `AcctKey` | Track 2 de tarjeta magnética: `PAN=FechaVen+Datos_adicionales` |
| `OriginatorType: 021` | Código de originador ATM (estándar ISO 8583) |
| `OriginatorType: 010` | Código de originador para depósito en ATM |
| `BankId: 00010016` | Código del Banco de Bogotá en el sistema |
| `X-IBM-Client-Id` | Client ID de IBM API Connect (gateway de la API) |
| `IncocredCode` | Código interno de red Incocred (red de cajeros) |
| `CityId: 11001` | Código DANE de Bogotá D.C. |
| `POSEntryMode: 010` | Modo entrada POS: tarjeta leída manualmente |
| `TrnRqUID` | Transaction Request Unique Identifier (idempotencia) |

---

## 7. Análisis de Seguridad

| Aspecto | Estado | Recomendación |
|---------|--------|---------------|
| Token Bearer en RETIRO/DEPOSITO/PAGO_OBLIGACIONES | ✅ Usa variable de entorno | Correcto |
| Token Bearer en CONSULTA_FACTURA y PAGO_FACTURA | ⚠️ Hardcodeado como `Bearer x` | Migrar a variable de entorno |
| PAN de tarjeta en body | ⚠️ En claro (`4915110205551818`) | Considerar tokenización en logs |
| Track 2 en body | ⚠️ En claro en `AcctKey` | Dato sensible PCI-DSS |
| Número de cuenta en CONSULTA_FACTURA | ✅ Enmascarado (`*****4207`) | Correcto para consulta |
| HTTPS obligatorio | ✅ Todas las peticiones usan HTTPS | Correcto |
| X-IBM-Client-Id compartido | ⚠️ Mismo valor en todas las peticiones | Verificar si es correcto por ambiente |

---

## 8. Recomendaciones para Automatización

### 8.1 Variables de Entorno Necesarias

```
bearer_token_0gro    = <token JWT válido>
base_url             = https://d2q3sea1wnkwiy.cloudfront.net
ibm_client_id        = ccc7806154afefbbe6a3c1c2a2ffb8e8
```

### 8.2 Correcciones Previas a Automatizar

1. **CONSULTA_FACTURA**: Cambiar `Authorization: Bearer x` por `Bearer {{bearer_token_0gro}}`
2. **PAGO_FACTURA**: Cambiar `Authorization: Bearer x` por `Bearer {{bearer_token_0gro}}`
3. **PAGO_FACTURA**: Corregir headers inválidos: `X-Channel`, `X-CompanyId`, `X-IPAddr`, `X-NextDt`
4. **PAGO_OBLIGACIONES**: Corregir headers inválidos: `X-Channel`, `X-CompanyId`
5. **CONSULTA_FACTURA**: Estandarizar `banco` a `BANCO_BOGOTA` o confirmar si `bbogota` es correcto
6. **CONSULTA_FACTURA / PAGO_FACTURA**: Confirmar si `obj_operacion` vs `operacionobj` es intencional
7. **Todas**: Actualizar fechas vencidas (`ExpDt: 2020-*`) a fechas vigentes
8. **PAGO_FACTURA**: Confirmar si `CurCode: 170` es correcto o debe ser `COP`

### 8.3 Flujo de Prueba Sugerido

```
Flujo ATM:
  1. Ejecutar RETIRO    → Validar respuesta exitosa + débito en cuenta
  2. Ejecutar DEPOSITO  → Validar respuesta exitosa + crédito en cuenta
  3. Ejecutar PAGO_OBLIGACIONES → Validar pago de obligación

Flujo Recaudo:
  1. Ejecutar CONSULTA_FACTURA  → Capturar datos de la factura
  2. Ejecutar PAGO_FACTURA      → Usar datos capturados del paso 1
```

---

## 9. Resumen de Hallazgos

| Categoría | Total | Críticos | Moderados | Informativos |
|-----------|-------|----------|-----------|--------------|
| Inconsistencias de estructura | 8 | 2 | 4 | 2 |
| Problemas de autenticación | 2 | 2 | 0 | 0 |
| Headers inválidos | 6 | 3 | 3 | 0 |
| Fechas vencidas | 2 | 0 | 2 | 0 |
| **TOTAL** | **18** | **7** | **9** | **2** |

### Hallazgos Críticos (requieren corrección antes de ejecutar)

1. **[A-01/A-02]** Tokens hardcodeados `Bearer x` en CONSULTA_FACTURA y PAGO_FACTURA — las peticiones fallarán por autenticación
2. **[H-03/H-04]** Headers `X-IPAddr: 677678` y `X-NextDt: 6756789` en PAGO_FACTURA son valores completamente inválidos
3. **[I-01]** La clave del body `obj_operacion` en CONSULTA_FACTURA vs `operacionobj` en el resto puede causar errores si el API es sensible al nombre del campo
4. **[I-02]** El typo `NetwokInfo` podría causar que el servidor no procese el bloque de red correctamente
