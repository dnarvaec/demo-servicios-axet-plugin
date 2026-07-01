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

    // ── URLs base ─────────────────────────────────────────────────────────────
    public static final String JSONPLACEHOLDER = "https://jsonplaceholder.typicode.com";
    public static final String REQRES          = "https://reqres.in/api";
    public static final String PETSTORE        = "https://petstore.swagger.io/v2";

    // ── JSONPlaceholder — paths ───────────────────────────────────────────────
    public static final class Posts {
        public static final String ALL    = "/posts";
        public static final String BY_ID  = "/posts/{id}";
    }

    public static final class Users {
        public static final String ALL    = "/users";
        public static final String BY_ID  = "/users/{id}";
    }

    public static final class Comments {
        public static final String ALL    = "/comments";
        public static final String BY_ID  = "/comments/{id}";
    }

    public static final class Todos {
        public static final String ALL    = "/todos";
        public static final String BY_ID  = "/todos/{id}";
    }

    // ── ReqRes — paths ────────────────────────────────────────────────────────
    public static final class ReqresUsers {
        public static final String ALL    = "/users";
        public static final String BY_ID  = "/users/{id}";
    }

    public static final class Auth {
        public static final String LOGIN    = "/login";
        public static final String REGISTER = "/register";
    }

    // ── Petstore — paths ──────────────────────────────────────────────────────
    public static final class Pet {
        public static final String ALL           = "/pet";
        public static final String BY_ID         = "/pet/{petId}";
        public static final String FIND_BY_STATUS = "/pet/findByStatus";
    }

    public static final class StoreInventory {
        public static final String INVENTORY = "/store/inventory";
    }
}
