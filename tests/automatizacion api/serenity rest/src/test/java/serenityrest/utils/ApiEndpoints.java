package serenityrest.utils;

/**
 * ApiEndpoints
 * ─────────────────────────────────────────────────────────────────────────────
 * Fuente de verdad única para URLs base y paths de endpoints.
 * NUNCA hardcodear URLs en Tasks, Interactions ni Step Definitions.
 *
 * PROYECTO : Everest — Automatización API Grupo Aval
 * HOST     : https://api.aval.nttdataco.com
 *
 * Transacciones cubiertas:
 *   TX-01  Retiro de efectivo (OTP)               X-RqUID: 001001
 *   TX-02  Depósitos y consignaciones (Efectivo)   X-RqUID: 002001
 *   TX-03  Recaudo de convenios (Efectivo)          X-RqUID: 003001
 *   TX-04  Pago de obligaciones y TC Aval (Efect.) X-RqUID: 004001
 *
 * Flujo TX-03 y TX-04 (dos pasos):
 *   1º Consultas.CONSULTA_FACTURA  (orquestador)
 *   2º Pagos.PAGO_FACTURA
 * ─────────────────────────────────────────────────────────────────────────────
 */
public final class ApiEndpoints {

    private ApiEndpoints() {}

    // ── URL base — host compartido de todos los endpoints Everest/Aval ────────
    public static final String API_BASE_URL = "https://api.aval.nttdataco.com";

    // ── Pagos — endpoints bajo /api/v1/pagos/ ─────────────────────────────────
    /**
     * Agrupa los paths del módulo de pagos (TX-01, TX-02, TX-03 paso 2, TX-04 paso 2).
     * Los paths son relativos a API_BASE_URL.
     */
    public static final class Pagos {
        /** TX-01 — Retiro de efectivo con OTP.
         *  POST https://api.aval.nttdataco.com/api/v1/pagos/retiro
         *  X-RqUID incremental: 001001 */
        public static final String RETIRO        = "/api/v1/pagos/retiro";

        /** TX-02 — Depósitos y consignaciones (Efectivo).
         *  POST https://api.aval.nttdataco.com/api/v1/pagos/deposito
         *  X-RqUID incremental: 002001 */
        public static final String DEPOSITO      = "/api/v1/pagos/deposito";

        /** TX-03 paso 2 / TX-04 paso 2 — Pago de factura / convenios / TC Aval.
         *  POST https://api.aval.nttdataco.com/api/v1/pagos/pago-factura
         *  X-RqUID incremental: 003001 (recaudo) | 004001 (pago oblig.) */
        public static final String PAGO_FACTURA  = "/api/v1/pagos/pago-factura";
    }

    // ── Consultas — endpoint orquestador bajo /everest/orq/consultas/ ─────────
    /**
     * Agrupa los paths del módulo de consultas (TX-03 paso 1, TX-04 paso 1).
     * Los paths son relativos a API_BASE_URL.
     */
    public static final class Consultas {
        /** TX-03 paso 1 / TX-04 paso 1 — Consulta de factura (orquestador Everest).
         *  POST https://api.aval.nttdataco.com/everest/orq/consultas/api/v1/consulta
         *  Se ejecuta SIEMPRE antes de Pagos.PAGO_FACTURA. */
        public static final String CONSULTA_FACTURA = "/everest/orq/consultas/api/v1/consulta";
    }

    // ── Auth — paths de autenticación (si aplica en futuros sprints) ──────────
    public static final class Auth {
        /** POST — Login, devuelve token de sesión */
        public static final String LOGIN    = "/login";
        /** POST — Registro de nuevo usuario */
        public static final String REGISTER = "/register";
        /** POST — Renovar token */
        public static final String REFRESH  = "/refresh";
    }
}
