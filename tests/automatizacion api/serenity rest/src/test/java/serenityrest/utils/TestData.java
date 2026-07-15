package serenityrest.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TestData — Everest / Grupo Aval
 * Builders de payloads y headers por transacción. NUNCA construir payloads inline.
 *
 * TX-01  retiroOtpPayload()       POST /api/v1/pagos/retiro          X-RqUID: 001001
 * TX-02  depositoPayload()        POST /api/v1/pagos/deposito         X-RqUID: 002001
 * TX-03  consultaFacturaPayload() POST /everest/orq/.../consulta      X-RqUID: 003001 (paso 1)
 *        pagoFacturaPayload()     POST /api/v1/pagos/pago-factura     X-RqUID: 003001 (paso 2)
 * TX-04  consultaFacturaPayload() POST /everest/orq/.../consulta      X-RqUID: 004001 (paso 1)
 *        pagoObligacionPayload()  POST /api/v1/pagos/pago-factura     X-RqUID: 004001 (paso 2)
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

    // ── Builders de NetworkTrnInfo compartido ─────────────────────────────────

    private static Map<String, Object> networkTrnInfo() {
        return map(
            "NetworkOwner",    "string",
            "OriginatorName",  "string",
            "OriginatorType",  "string",
            "TerminalId",      "string",
            "NetworkRefId",    "string",
            "TeminalSequence", "string",
            "IncocredCode",    "string",
            "PostAddr",        map("Addr1", "string", "StateProv", "string")
        );
    }

    private static Map<String, Object> partyAcctRelInfo() {
        return map(
            "DepAcctIdFrom",  map("DepAcctId",  map("AcctType", "string", "AcctKey", "string",
                                                    "BankInfo",  map("BankId", "string"))),
            "CardAcctIdFrom", map("CardAcctId", map("AcctId", "string"))
        );
    }

    private static Map<String, Object> depAcctIdMain() {
        return map("AcctType", "string", "AcctKey", "string", "BankInfo", map("BankId", "string"));
    }

    private static Map<String, Object> contactInfo() {
        return map("PhoneNum", map("PhoneType", "string", "Phone", "string"));
    }

    private static Map<String, Object> curAmt() {
        return map("Amt", 0, "CurCode", "string");
    }

    private static Map<String, Object> fee() {
        return map("CurAmt", curAmt());
    }

    // =========================================================================
    // TX-01 — Retiro de efectivo (OTP)
    // =========================================================================

    public static Map<String, String> retiroHeaders() {
        return strMap(
            "Content-Type",     "application/json",
            "X-Transaction-Id", "1234567890",
            "X-RqUID",          "001001",
            "X-Channel",        "ATM",
            "X-CompanyId",      "BANCO_BOGOTA",
            "X-IPAddr",         "192.168.0.10",
            "X-NextDt",         "2026-07-14T11:31:00-05",
            "X-ClientDt",       "client-app-id",
            "X-CustIdentType",  "CC",
            "X-CustIdentNum",   "123456789",
            "X-SessKey",        "session-key-xyz",
            "X-Language",       "ES",
            "X-CustLoginId",    "user-login-id"
        );
    }

    public static Map<String, Object> retiroOtpPayload() {
        Map<String, Object> op = new HashMap<>();
        op.put("NetworkTrnInfo",   networkTrnInfo());
        op.put("PartyAcctRelInfo", partyAcctRelInfo());
        op.put("DepAcctId",        depAcctIdMain());
        op.put("ContactInfo",      contactInfo());
        op.put("CurAmt",           curAmt());
        op.put("Fee",              fee());
        op.put("OTPInfo",          map("OtpType", "string", "OtpValue", "string"));
        return map("banco", "BANCO_BOGOTA", "operacion", "RETIRO", "operacionobj", op);
    }

    // =========================================================================
    // TX-02 — Depósitos y consignaciones (Efectivo)
    // =========================================================================

    public static Map<String, String> depositoHeaders() {
        return strMap(
            "Content-Type",     "application/json",
            "X-Transaction-Id", "1234567890",
            "X-RqUID",          "002001",
            "X-Channel",        "ATM",
            "X-CompanyId",      "BANCO_BOGOTA",
            "X-IPAddr",         "192.168.0.10",
            "X-NextDt",         "2026-07-14T11:34:00-05",
            "X-ClientDt",       "client-app-id",
            "X-CustIdentType",  "CC",
            "X-CustIdentNum",   "123456789",
            "X-SessKey",        "session-key-xyz",
            "X-Language",       "ES",
            "X-CustLoginId",    "user-login-id"
        );
    }

    public static Map<String, Object> depositoPayload() {
        Map<String, Object> op = new HashMap<>();
        op.put("NetworkTrnInfo",   networkTrnInfo());
        op.put("PartyAcctRelInfo", partyAcctRelInfo());
        op.put("DepAcctId",        depAcctIdMain());
        op.put("ContactInfo",      contactInfo());
        op.put("CurAmt",           curAmt());
        op.put("Fee",              fee());
        // Sin OTPInfo — modalidad Efectivo
        return map("banco", "BANCO_BOGOTA", "operacion", "DEPOSITO", "operacionobj", op);
    }

    // =========================================================================
    // TX-03/TX-04 Paso 1 — Consulta factura (orquestador Everest)
    // Ejecutar SIEMPRE antes de pagoFacturaPayload() o pagoObligacionPayload()
    // =========================================================================

    /**
     * @param xRqUID  "003001" para TX-03 (recaudo) | "004001" para TX-04 (pago oblig.)
     */
    public static Map<String, String> consultaFacturaHeaders(String xRqUID) {
        return strMap(
            "Content-Type",    "application/json",
            "X-RqUID",         xRqUID,
            "X-Channel",       "CBV",
            "X-ClientDt",      "2022-06-17T11:25:14",
            "X-NextDt",        "2022-06-17T11:25:14",
            "X-CustIdentType", "CC",
            "X-CustIdentNum",  "10123377654",
            "X-CompanyId",     "00010016",
            "X-IPAddr",        "192.168.0.10",
            "Authorization",   "Bearer AAIgZjYzODEyYjk3M2RmNDhmM2Q5ODUzMGVkZTk5ZDAwOTWC_C8R6FzbVdMhaIWFJaBQ7PWHrcXPAQm4jPOOJFTtF8eBJAUiA_j0v",
            "X-IBM-Client-Id", "f638122974df4813r98530ede99d0a93"
        );
    }

    public static Map<String, Object> consultaFacturaPayload() {
        Map<String, Object> agreement = map(
            "agrmId",     "7946",
            "invoiceNum", "491511***************************0000",
            "expDt",      "2020-06-24T16:05:45.314",
            "cSPRefId",   "12",
            "depAcctId",  map("acctId", "*****4207", "acctType", "CCA")
        );
        Map<String, Object> invoiceSender = map(
            "acctPayAcct",    "1003214830",
            "svcId",          "0007",
            "invSndrPmtInfo", map("posEntryMode", "010"),
            "agrmType",       "1"
        );
        Map<String, Object> objOperacion = new HashMap<>();
        objOperacion.put("netwokInfo",    map("networkOwner", "7946", "networkRefId", "7946"));
        objOperacion.put("transaction",   map("trnRqUID", "7946", "trnSrc", "BMOB", "terminalSequence", "4594971"));
        objOperacion.put("agreement",     agreement);
        objOperacion.put("invoiceSender", invoiceSender);
        objOperacion.put("pspCity",       map("cityId", "11001"));
        objOperacion.put("locationInfo",  map("geoLocation", "KR 11 # 71 -73"));
        return map("banco", "bbogota", "operacion", "CONSULTA_FACTURA", "obj_operacion", objOperacion);
    }

    // =========================================================================
    // TX-03 Paso 2 — Recaudo de convenios (Efectivo)
    // =========================================================================

    public static Map<String, String> pagoFacturaHeaders() {
        return strMap(
            "Content-Type",     "application/json",
            "X-Transaction-Id", "78789",
            "X-RqUID",          "003001",
            "X-Channel",        "ATM",
            "X-CompanyId",      "BANCO_BOGOTA",
            "X-IPAddr",         "192.168.0.10",
            "X-NextDt",         "2026-07-14T11:35:00-05"
        );
    }

    public static Map<String, Object> pagoFacturaPayload() {
        return buildPagoFacturaBody("PAGO_FACTURA");
    }

    // =========================================================================
    // TX-04 Paso 2 — Pago de obligaciones y Tarjeta de Crédito Aval (Efectivo)
    // =========================================================================

    public static Map<String, String> pagoObligacionHeaders() {
        return strMap(
            "Content-Type",     "application/json",
            "X-Transaction-Id", "78789",
            "X-RqUID",          "004001",
            "X-Channel",        "ATM",
            "X-CompanyId",      "BANCO_BOGOTA",
            "X-IPAddr",         "192.168.0.10",
            "X-NextDt",         "2026-07-14T11:36:00-05"
        );
    }

    public static Map<String, Object> pagoObligacionPayload() {
        return buildPagoFacturaBody("PAGO_OBLIGACION");
    }

    // ── Builder compartido para pago-factura ──────────────────────────────────

    private static Map<String, Object> buildPagoFacturaBody(String operacion) {
        Map<String, Object> refInfo = map("RefType", "string", "RefId", "string");
        List<Map<String, Object>> refInfoList = new ArrayList<>();
        refInfoList.add(refInfo);

        Map<String, Object> txn = new HashMap<>();
        txn.put("TrnRqUID",        "string");
        txn.put("TrnSrc",          "string");
        txn.put("ClientDt",        "string");
        txn.put("RefInfo",         refInfoList);
        txn.put("ApprovalId",      "string");
        txn.put("CurAmt",          curAmt());
        txn.put("TerminalSequence","string");

        Map<String, Object> agreement = map(
            "NIE",           "string",
            "Name",          "string",
            "SPAdditionalId","string",
            "PmtCodServ",    "string",
            "AgrmId",        "string",
            "InvoiceNum",    "string",
            "ExpDt",         "string",
            "CSPRefId",      "string",
            "DepAcctId",     map("AcctId", "string")
        );

        Map<String, Object> acctBal = map("CurAmt", curAmt(), "Desc", "string");
        List<Map<String, Object>> acctBalList = new ArrayList<>();
        acctBalList.add(acctBal);

        Map<String, Object> operacionobj = new HashMap<>();
        operacionobj.put("NetwokInfo",     map("NetworkOwner", "string", "NetworkRefId", "string"));
        operacionobj.put("Transaction",    txn);
        operacionobj.put("Agreement",      agreement);
        operacionobj.put("TotalCurAmt",    curAmt());
        operacionobj.put("InvoiceSender",  map("AcctPayAcct", "string", "AgrmType", "string",
                                               "SvcId", "string",
                                               "InvSndrPmtInfo", map("POSEntryMode", "string")));
        operacionobj.put("PSPCity",        map("CityId", "string"));
        operacionobj.put("LocationInfo",   map("GeoLocation", "string"));
        operacionobj.put("AcctBal",        acctBalList);

        return map("banco", "BANCO_BOGOTA", "operacion", operacion, "operacionobj", operacionobj);
    }

    // ── Utilidad: nombre único para datos de prueba ───────────────────────────

    public static String uniqueName(String prefix) {
        return prefix + "-" + System.currentTimeMillis();
    }
}
