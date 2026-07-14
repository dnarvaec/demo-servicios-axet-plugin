package serenityrest.utils;

/**
 * ApiEndpoints
 * ─────────────────────────────────────────────────────────────────────────────
 * Fuente de verdad única para URLs base y paths de endpoints.
 * NUNCA hardcodear URLs en Tasks, Interactions ni Step Definitions.
 *
 * INSTRUCCIONES DE CONFIGURACIÓN:
 *   1. Actualizar API_BASE_URL con la URL base real del proyecto
 *   2. Crear clases internas estáticas por recurso (Auth, Users, Orders, etc.)
 *   3. Cada constante representa un path de endpoint (sin la URL base)
 * ─────────────────────────────────────────────────────────────────────────────
 */
public final class ApiEndpoints {

    private ApiEndpoints() {}

    // ── URL base — actualizar según el proyecto activo ────────────────────────
    /**
     * TODO: Reemplazar con la URL base de la API del proyecto.
     * Configurada también en serenity.conf → environments.dev.api.baseUrl
     */
    public static final String API_BASE_URL = "https://TODO-ACTUALIZAR-URL-BASE/api";

    // ── Autenticación — paths de auth (si aplica) ─────────────────────────────
    public static final class Auth {
        /** POST — Login, devuelve token de sesión */
        public static final String LOGIN    = "/login";
        /** POST — Registro de nuevo usuario */
        public static final String REGISTER = "/register";
        /** POST — Renovar token */
        public static final String REFRESH  = "/refresh";
    }

    // ── Recurso genérico — reemplazar con el recurso real del proyecto ────────
    // Ejemplo: public static final class Usuarios { ... }
    // Ejemplo: public static final class Pedidos  { ... }

    // ── JSONPlaceholder — endpoints de referencia para demos/pruebas ──────────
    public static final String JSONPLACEHOLDER = "https://jsonplaceholder.typicode.com";

    public static final class Posts {
        public static final String ALL   = "/posts";
        public static final String BY_ID = "/posts/{id}";
    }

    public static final class Users {
        public static final String ALL   = "/users";
        public static final String BY_ID = "/users/{id}";
    }

    public static final class Todos {
        public static final String ALL   = "/todos";
        public static final String BY_ID = "/todos/{id}";
    }
}
