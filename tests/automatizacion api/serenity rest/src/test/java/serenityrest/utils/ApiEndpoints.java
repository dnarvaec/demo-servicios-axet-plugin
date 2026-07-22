package serenityrest.utils;

/**
 * ApiEndpoints
 * ─────────────────────────────────────────────────────────────────────────────
 * Fuente de verdad única para URLs base y paths de endpoints.
 * NUNCA hardcodear URLs en Tasks, Interactions ni Step Definitions.
 *
 * PROYECTO : Everest — Automatización API Grupo Aval
 * HOST     : https://api.aval.nttdatacolombia.com
 *
 * Transacciones cubiertas:
 *   TX-01  Retiro de efectivo (OTP)               X-RqUID: 001001
 *   TX-02  Depósitos y consignaciones (Efectivo)   X-RqUID: 002001
 *   TX-03  Recaudo de convenios (Efectivo)          X-RqUID: 003001
 *   TX-04  Pago de obligaciones y TC Aval (Efect.) X-RqUID: 004001
 *
 * Flujo TX-03 (dos pasos):
 *   1º Consultas.CONSULTA_FACTURA  (orquestador)
 *   2º Pagos.PAGO_FACTURA
 *
 * Flujo TX-04 (un paso directo):
 *   Pagos.PAGO_OBLIGACIONES
 * ─────────────────────────────────────────────────────────────────────────────
 */
public final class ApiEndpoints {

    private ApiEndpoints() {}

    // ── URL base — host compartido de todos los endpoints Everest/Aval ────────
    public static final String API_BASE_URL = "https://d2q3sea1wnkwiy.cloudfront.net";

    // ── Pagos — endpoints bajo /api/v1/pagos/ ─────────────────────────────────
    /**
     * Agrupa los paths del módulo de pagos (TX-01, TX-02, TX-03 paso 2, TX-04 directo).
     * Los paths son relativos a API_BASE_URL.
     */
    public static final class Pagos {
        /** TX-01 — Retiro de efectivo con OTP.
         *  POST https://api.aval.nttdatacolombia.com/api/v1/pagos/retiro
         *  X-RqUID incremental: 001001 */
        public static final String RETIRO        = "/api/v1/pagos/retiro";

        /** TX-02 — Depósitos y consignaciones (Efectivo).
         *  POST https://api.aval.nttdatacolombia.com/api/v1/pagos/deposito
         *  X-RqUID incremental: 002001 */
        public static final String DEPOSITO      = "/api/v1/pagos/deposito";


        /** TX-03 paso 2 / TX-04 paso 2 — Pago de factura / convenios / TC Aval.
         *  POST https://api.aval.nttdatacolombia.com/api/v1/pagos/pago-factura
         *  X-RqUID incremental: 003001 (recaudo) | 004001 (pago oblig.) */
        public static final String PAGO_FACTURA  = "/api/v1/pagos/pago-factura";

        /** TX-03 paso 2 — Pago de factura / convenios (Efectivo).
         *  POST https://api.aval.nttdatacolombia.com/api/v1/pagos/pago-factura
         *  X-RqUID incremental: 003001 (recaudo) */
        

        /** TX-04 — Pago de obligaciones y TC Aval (Efectivo).
         *  POST https://api.aval.nttdatacolombia.com/api/v1/pagos/pago-obligaciones
         *  X-RqUID incremental: 004001 */
        public static final String PAGO_OBLIGACIONES = "/api/v1/pagos/pago-obligaciones";
        
    }

    // ── Consultas — endpoint orquestador bajo /everest/orq/consultas/ ─────────
    /**
     * Agrupa los paths del módulo de consultas (TX-03 paso 1 únicamente).
     * Los paths son relativos a API_BASE_URL.
     * TX-04 NO usa este endpoint — es un endpoint directo: Pagos.PAGO_OBLIGACIONES.
     */
    public static final class Consultas {

        /** TX-03 paso 1 / TX-04 paso 1 — Consulta de factura (orquestador Everest).
         *  POST https://api.aval.nttdatacolombia.com/everest/orq/consultas/api/v1/consulta
         *  Se ejecuta SIEMPRE antes de los Pagos.PAGO_FACTURA. */


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
