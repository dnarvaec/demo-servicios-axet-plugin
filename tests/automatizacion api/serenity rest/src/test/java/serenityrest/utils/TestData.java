package serenityrest.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * TestData
 * ─────────────────────────────────────────────────────────────────────────────
 * Builders de payloads de prueba reutilizables.
 * Centraliza la construcción de bodies de request para Tasks.
 * NUNCA construir payloads inline en Step Definitions ni Tasks.
 * ─────────────────────────────────────────────────────────────────────────────
 */
public final class TestData {

    private TestData() {}

    // ── Posts (JSONPlaceholder) ───────────────────────────────────────────────

    public static Map<String, Object> createPostPayload(String title, String body, int userId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", title);
        payload.put("body", body);
        payload.put("userId", userId);
        return payload;
    }

    public static Map<String, Object> defaultCreatePostPayload() {
        return createPostPayload(
            "Demo Serenity Screenplay",
            "Automatización API con Serenity BDD — Screenplay Pattern",
            1
        );
    }

    public static Map<String, Object> patchPostPayload(String title) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", title);
        return payload;
    }

    // ── Users (ReqRes) ────────────────────────────────────────────────────────

    public static Map<String, Object> createUserPayload(String name, String job) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("job", job);
        return payload;
    }

    public static Map<String, Object> defaultCreateUserPayload() {
        return createUserPayload("Demo User Serenity", "QA Automation Engineer");
    }

    // ── Auth (ReqRes) ─────────────────────────────────────────────────────────

    public static Map<String, Object> loginPayload(String email, String password) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("password", password);
        return payload;
    }

    public static Map<String, Object> defaultLoginPayload() {
        String email    = System.getenv("REQRES_EMAIL")    != null ? System.getenv("REQRES_EMAIL")    : "eve.holt@reqres.in";
        String password = System.getenv("REQRES_PASSWORD") != null ? System.getenv("REQRES_PASSWORD") : "cityslicka";
        return loginPayload(email, password);
    }

    // ── Generador de valores únicos ───────────────────────────────────────────

    public static String uniqueName(String prefix) {
        return prefix + "-" + System.currentTimeMillis();
    }
}
