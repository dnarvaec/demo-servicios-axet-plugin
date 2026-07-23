package serenityrest.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        Map<String, Object> networkTrnInfo = map(
            "OriginatorName",  "BMOB",
            "OriginatorType",  "021",
            "TerminalId",      "00BOG138",
            "NetworkRefId",    "7946",
            "TeminalSequence", "6032",
            "IncocredCode",    "457896",
            "PostAddr",        map("Addr1", "Direccion", "StateProv", "2302")
        );

        Map<String, Object> partyAcctRelInfo = map(
            "DepAcctIdFrom",  map("DepAcctId",  map(
                                  "AcctType", "DDA",
                                  "AcctKey",  "4915110205551818=23121011339517000000")),
            "CardAcctIdFrom", map("CardAcctId", map("AcctId", "4915110205551818"))
        );

        Map<String, Object> depAcctId = map(
            "AcctType", "DDA",
            "BankInfo", map("BankId", "00010016")
        );

        Map<String, Object> contactInfo = map(
            "PhoneNum", map("PhoneType", "Celular", "Phone", "3118451263")
        );

        Map<String, Object> curAmt = map("Amt", 20000.00, "CurCode", "COP");
        Map<String, Object> fee    = map("CurAmt", map("Amt", 1800.00, "CurCode", "COP"));
        Map<String, Object> otpInfo = map("OtpType", "OTP", "OtpValue", "1245");

        Map<String, Object> op = new HashMap<>();
        op.put("NetworkTrnInfo",   networkTrnInfo);
        op.put("PartyAcctRelInfo", partyAcctRelInfo);
        op.put("DepAcctId",        depAcctId);
        op.put("ContactInfo",      contactInfo);
        op.put("CurAmt",           curAmt);
        op.put("Fee",              fee);
        op.put("OTPInfo",          otpInfo);

        return map("banco", "BANCO_BOGOTA", "operacion", "RETIRO", "operacionobj", op);
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
        Map<String, Object> networkTrnInfo = map(
            "OriginatorName",  "BMOB",
            "OriginatorType",  "010",
            "TerminalId",      "00BOG138",
            "NetworkRefId",    "7948",
            "TeminalSequence", "603237",
            "IncocredCode",    "184990",
            "PostAddr",        map("Addr1", "Direccion 43", "StateProv", "2302")
        );

        Map<String, Object> partyAcctRelInfo = map(
            "DepAcctIdFrom",  map("DepAcctId",  map(
                                  "AcctType", "DDA",
                                  "AcctKey",  "4473990520002999=99990000000000000000")),
            "CardAcctIdFrom", map("CardAcctId", map("AcctId", "4473990520002999"))
        );

        Map<String, Object> depAcctId = map(
            "AcctId",   "0015423459",
            "AcctType", "DDA",
            "BankInfo", map("BankId", "00010016")
        );

        Map<String, Object> contactInfo = map(
            "PhoneNum", map("PhoneType", "Celular", "Phone", "3118451263")
        );

        Map<String, Object> curAmt = map("Amt", 20000.00, "CurCode", "COP");
        Map<String, Object> fee    = map("CurAmt", map("Amt", 1800.00, "CurCode", "COP"));

        Map<String, Object> op = new HashMap<>();
        op.put("NetworkTrnInfo",   networkTrnInfo);
        op.put("PartyAcctRelInfo", partyAcctRelInfo);
        op.put("DepAcctId",        depAcctId);
        op.put("ContactInfo",      contactInfo);
        op.put("CurAmt",           curAmt);
        op.put("Fee",              fee);
        // Sin OTPInfo — DEPOSITO no requiere OTP

        return map("banco", "BANCO_BOGOTA", "operacion", "DEPOSITO", "operacionobj", op);
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
        Map<String, Object> agreement = map(
            "AgrmId",    "7946",
            "InvoiceNum","123456789",
            "ExpDt",     "2020-06-24T16:05:45.314",
            "CSPRefId",  "12",
            "DepAcctId", map("AcctId", "*****4207", "AcctType", "CCA")
        );

        Map<String, Object> invoiceSender = map(
            "AcctPayAcct",    "1003214830",
            "SvcId",          "0007",
            "InvSndrPmtInfo", map("POSEntryMode", "010"),
            "AgrmType",       "1"
        );

        Map<String, Object> objOperacion = new HashMap<>();
        objOperacion.put("NetwokInfo",    map("NetworkOwner", "7946", "NetworkRefId", "7946"));
        objOperacion.put("Transaction",   map("TrnRqUID", "MOCK100", "TrnSrc", "BMOB", "TerminalSequence", "4594971"));
        objOperacion.put("Agreement",     agreement);
        objOperacion.put("InvoiceSender", invoiceSender);
        objOperacion.put("PSPCity",       map("CityId", "11001"));
        objOperacion.put("LocationInfo",  map("GeoLocation", "KR 11 # 71 -73"));

        return map("banco", "bbogota", "operacion", "CONSULTA_FACTURA", "obj_operacion", objOperacion);
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
        List<Map<String, Object>> refInfoList = new ArrayList<>();
        refInfoList.add(map("RefId", "10002795130011", "RefType", "Referencia3"));

        Map<String, Object> transaction = new HashMap<>();
        transaction.put("TrnRqUID",         "7946");
        transaction.put("TrnSrc",           "BMOB");
        transaction.put("TerminalSequence", "4595976");
        transaction.put("RefInfo",          refInfoList);

        Map<String, Object> agreement = map(
            "NIE",       "12053337612",
            "AgrmId",    "7946",
            "InvoiceNum","4915110205551818=23121011339517000000",
            "ExpDt",     "2020-06-24T20:21:03.314",
            "DepAcctId", map("AcctId", "821004207", "AcctType", "CCA")
        );

        Map<String, Object> invoiceSender = map(
            "AcctPayAcct",    "12053337612",
            "InvSndrPmtInfo", map("POSEntryMode", "010"),
            "SvcId",          "00007"
        );

        List<Map<String, Object>> acctBalList = new ArrayList<>();
        acctBalList.add(map("Desc", "ValorPrincipal",  "CurAmt", map("Amt", 38020.00, "CurCode", "170")));
        acctBalList.add(map("Desc", "ValorAdicional1", "CurAmt", map("Amt", 16520.00, "CurCode", "170")));

        Map<String, Object> operacionobj = new HashMap<>();
        operacionobj.put("NetwokInfo",    map("NetworkRefId", "7946", "NetworkOwner", "7946"));
        operacionobj.put("Transaction",   transaction);
        operacionobj.put("TotalCurAmt",   map("Amt", 10000.00, "CurCode", "170"));
        operacionobj.put("Agreement",     agreement);
        operacionobj.put("InvoiceSender", invoiceSender);
        operacionobj.put("PSPCity",       map("CityId", "90025"));
        operacionobj.put("LocationInfo",  map("GeoLocation", "KR 11 # 71 -73"));
        operacionobj.put("AcctBal",       acctBalList);

        return map("banco", "BANCO_BOGOTA", "operacion", "PAGO_FACTURA", "operacionobj", operacionobj);
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
        Map<String, Object> networkTrnInfo = map(
            "OriginatorName",  "BMOB",
            "OriginatorType",  "021",
            "TerminalId",      "00BOG138",
            "NetworkRefId",    "7946",
            "TeminalSequence", "6032",
            "PostAddr",        map("Addr1", "Direccion", "StateProv", "2302")
        );

        Map<String, Object> loanPmtInfo = map(
            "DepAcctIdFrom",     map("DepAcctId", map(
                                     "AcctType", "DDA",
                                     "AcctKey",  "4915110205551818=23121011339517000000")),
            "DepAcctIdTo",       map("DepAcctId", map(
                                     "AcctId",   "200000100000900591657949",
                                     "BankInfo", map("BankId", "00010016"))),
            "CurAmt",            map("Amt", 20000.00, "CurCode", "COP"),
            "LoanPmtType",       "CCA",
            "LoanPmtComplement", "7946",
            "CardAcctIdFrom",    map("CardAcctId", map("AcctId", "4915110205551818"))
        );

        Map<String, Object> operacionobj = map(
            "NetworkTrnInfo", networkTrnInfo,
            "LoanPmtInfo",    loanPmtInfo
        );

        return map("banco", "BANCO_BOGOTA", "operacion", "PAGO_OBLIGACIONES", "operacionobj", operacionobj);
    }

    // ── Utilidad: nombre único para datos de prueba ───────────────────────────

    public static String uniqueName(String prefix) {
        return prefix + "-" + System.currentTimeMillis();
    }
}
