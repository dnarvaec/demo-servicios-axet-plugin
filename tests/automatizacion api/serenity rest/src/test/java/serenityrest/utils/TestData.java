package serenityrest.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * TestData — Everest / Grupo Aval
 * Builders de payloads y headers por transacción. NUNCA construir payloads inline.
 * Datos alineados con la colección Postman "Everest AVC" (Postman v2.1.0).
 *
 * TX-01  retiroOtpPayload()       POST /api/v1/pagos/retiro          X-RqUID: 001001
 * TX-02  depositoPayload()        POST /api/v1/pagos/deposito         X-RqUID: 002001
 * TX-03  consultaFacturaPayload() POST /everest/orq/.../consulta      X-RqUID: 003001 (paso 1)
 *        pagoFacturaPayload()     POST /api/v1/pagos/pago-factura     X-RqUID: 003001 (paso 2)
 * TX-04  pagoObligacionPayload()  POST /api/v1/pagos/pago-obligaciones X-RqUID: 004001
 */
public final class TestData {

    private TestData() {}

    // ── Utilidad interna ──────────────────────────────────────────────────────

    private static Map<String, Object> map(Object... pairs) {
        Map<String, Object> m = new HashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            m.put((String) pairs[i], pairs[i + 1]);
        }
        return m;
    }

    private static Map<String, String> strMap(Object... pairs) {
        Map<String, String> m = new HashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            m.put((String) pairs[i], (String) pairs[i + 1]);
        }
        return m;
    }

    // =========================================================================
    // TX-01 — Retiro de efectivo (OTP)
    // Colección: RETIRO — POST /api/v1/pagos/retiro
    // =========================================================================

    public static Map<String, String> retiroHeaders() {
        return strMap(
            "Content-Type",     "application/json",
            "X-Transaction-Id", "1234567890",
            "X-RqUID",          "001001",
            "X-Channel",        "ATM",
            "X-CompanyId",      "BANCO_BOGOTA",
            "X-IPAddr",         "192.168.0.10",
            "X-IP-client",      "192.168.0.10",
            "X-NextDt",         "2026-07-14T11:31:00-05:00",
            "X-ClientDt",       "2026-07-14T11:31:00-05:00",
            "X-CustIdentType",  "CC",
            "X-CustIdentNum",   "123456789",
            "X-SessKey",        "session-key-xyz",
            "X-Language",       "ES",
            "X-CustLoginId",    "user-login-id",
            "X-IBM-Client-Id",  "ccc7806154afefbbe6a3c1c2a2ffb8e8",
            "X-Device-ID",      "ATM-BOG-138"
        );
    }

    public static Map<String, Object> retiroOtpPayload() {
        return DataDrivenExcelReader.retiroPayload();
    }

    // =========================================================================
    // TX-02 — Depósitos y consignaciones (Efectivo)
    // Colección: DEPOSITO — POST /api/v1/pagos/deposito
    // =========================================================================

    public static Map<String, String> depositoHeaders() {
        return strMap(
            "Content-Type",     "application/json",
            "X-Transaction-Id", "1234567890",
            "X-RqUID",          "002001",
            "X-Channel",        "ATM",
            "X-CompanyId",      "BANCO_BOGOTA",
            "X-IPAddr",         "192.168.0.10",
            "X-IP-client",      "192.168.0.10",
            "X-NextDt",         "2026-07-14T11:34:00-05:00",
            "X-ClientDt",       "2026-07-14T11:34:00-05:00",
            "X-CustIdentType",  "CC",
            "X-CustIdentNum",   "123456789",
            "X-SessKey",        "session-key-xyz",
            "X-Language",       "ES",
            "X-CustLoginId",    "user-login-id",
            "X-IBM-Client-Id",  "ccc7806154afefbbe6a3c1c2a2ffb8e8",
            "X-Device-ID",      "ATM-BOG-138"
        );
    }

    public static Map<String, Object> depositoPayload() {
        return DataDrivenExcelReader.depositoPayload();
    }

    // =========================================================================
    // TX-03 Paso 1 — Consulta factura (orquestador Everest)
    // Colección: CONSULTA_FACTURA — POST /everest/orq/consultas/api/v1/consulta
    // Ejecutar SIEMPRE antes de pagoFacturaPayload() en el flujo TX-03.
    // =========================================================================

    public static Map<String, String> consultaFacturaHeaders(String xRqUID) {
        return strMap(
            "Content-Type",    "application/json",
            "Authorization",   "Bearer x",
            "X-RqUID",         xRqUID,
            "X-Channel",       "CBV",
            "X-ClientDt",      "2026-07-22T12:00:00",
            "X-NextDt",        "2026-07-22T12:00:00",
            "X-CustIdentType", "CC",
            "X-CustIdentNum",  "10123377654",
            "X-IBM-Client-Id", "ccc7806154afefbbe6a3c1c2a2ffb8e8",
            "X-CompanyId",     "00010016",
            "X-IPAddr",        "192.168.1.100",
            "X-IP-client",     "192.168.1.100",
            "X-Device-ID",     "TEST-DEVICE-001"
        );
    }

    public static Map<String, Object> consultaFacturaPayload() {
        return consultaFacturaPayload("7946");
    }

    public static Map<String, Object> consultaFacturaPayload(String trnRqUID) {
        return DataDrivenExcelReader.consultaFacturaPayload(trnRqUID);
    }

    // =========================================================================
    // TX-03 Paso 2 — Recaudo de convenios / pago de factura (Efectivo)
    // Colección: PAGO_FACTURA — POST /api/v1/pagos/pago-factura
    // =========================================================================

    public static Map<String, String> pagoFacturaHeaders() {
        return strMap(
            "Content-Type",     "application/json",
            "Authorization",    "Bearer x",
            "X-Transaction-Id", "78789",
            "X-RqUID",          "003001",
            "X-Channel",        "89098789",
            "X-CompanyId",      "789890",
            "X-IPAddr",         "677678",
            "X-NextDt",         "6756789",
            "X-IP-client",      "192.168.1.100",
            "X-Device-ID",      "TEST-DEVICE-001",
            "X-IBM-Client-Id",  "ccc7806154afefbbe6a3c1c2a2ffb8e8"
        );
    }

    public static Map<String, Object> pagoFacturaPayload() {
        return DataDrivenExcelReader.pagoFacturaPayload();
    }

    // =========================================================================
    // TX-04 — Pago de obligaciones y Tarjeta de Crédito Aval (Efectivo)
    // Colección: PAGO_OBLIGACIONES — POST /api/v1/pagos/pago-obligaciones
    // =========================================================================

    public static Map<String, String> pagoObligacionHeaders() {
        return strMap(
            "Content-Type",        "application/json",
            "X-Transaction-Id",    "78789",
            "X-RqUID",             "004001",
            "X-Channel",           "89098789",
            "X-CompanyId",         "789890",
            "X-IPAddr",            "192.168.1.100",
            "X-IP-client",         "192.168.1.100",
            "X-NextDt",            "2026-07-22T12:00:00-05:00",
            "X-ClientDt",          "2026-07-22T12:00:00-05:00",
            "X-IdentSerialNum",    "asd",
            "X-GovIssueIdentType", "qwe",
            "X-IBM-Client-Id",     "ccc7806154afefbbe6a3c1c2a2ffb8e8",
            "X-Device-ID",         "TEST-001"
        );
    }

    public static Map<String, Object> pagoObligacionPayload() {
        return DataDrivenExcelReader.pagoObligacionPayload();
    }

    // ── Utilidad: nombre único para datos de prueba ───────────────────────────

    public static String uniqueName(String prefix) {
        return prefix + "-" + System.currentTimeMillis();
    }
}
