package serenityrest.utils;

/**
 * AppUrls
 * ─────────────────────────────────────────────────────────────────────────────
 * URLs de las aplicaciones web (UI) — usadas por Tasks de Playwright.
 * Separado de ApiEndpoints para distinguir URLs de frontend vs. backend API.
 * ─────────────────────────────────────────────────────────────────────────────
 */
public final class AppUrls {

    private AppUrls() {}

    // ── Ecopetrol — App Central ───────────────────────────────────────────────
    /** URL base de la aplicación web Ecopetrol */
    public static final String ECOPETROL_BASE  =
            "https://app-central-ecopetrol-geeqdnayfth9d7cx.centralus-01.azurewebsites.net";

    /** Pantalla de login */
    public static final String ECOPETROL_LOGIN = ECOPETROL_BASE + "/login";

    /** Pantalla de bienvenida / selección de rol */
    public static final String ECOPETROL_HOME  = ECOPETROL_BASE + "/bienvenidos";
}
