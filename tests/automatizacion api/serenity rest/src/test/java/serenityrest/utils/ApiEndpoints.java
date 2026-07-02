package serenityrest.utils;

/**
 * ApiEndpoints
 * ─────────────────────────────────────────────────────────────────────────────
 * Fuente de verdad única para URLs base y paths de endpoints.
 * NUNCA hardcodear URLs en Tasks, Interactions ni Step Definitions.
 * ─────────────────────────────────────────────────────────────────────────────
 */
public final class ApiEndpoints {

    private ApiEndpoints() {}
    
    // ── Ecopetrol — API base y paths ──────────────────────────────────────────
    /** URL base de la API REST de Ecopetrol (prefijo /talento) */
    public static final String ECOPETROL_API =
            "https://app-central-ecopetrol-geeqdnayfth9d7cx.centralus-01.azurewebsites.net/talento";

    public static final class EcopetrolAuth {
        /** POST — Login con credenciales, devuelve token + datos del usuario */
        public static final String LOGIN = "/login";
    }
}
