package serenityrest.screenplay.pages;

/**
 * Page Object — Pantalla de Login de Ecopetrol
 * Selectores CSS derivados de inspección real en:
 * https://app-central-ecopetrol-geeqdnayfth9d7cx.centralus-01.azurewebsites.net/login
 *
 * Uso con Playwright: page.fill(LoginPage.CAMPO_USUARIO, value)
 */
public class LoginPage {

    /** Campo "Usuario" — placeholder real: "Ingresa tu usuario" */
    public static final String CAMPO_USUARIO   = "input[placeholder='Ingresa tu usuario']";

    /** Campo "Contraseña" — placeholder real: "Ingresa tu contraseña" */
    public static final String CAMPO_PASSWORD  = "input[placeholder='Ingresa tu contrase\u00f1a']";

    /** Botón "Iniciar sesión" — busca por texto visible (más robusto que clase CSS) */
    public static final String BOTON_LOGIN     = "button:has-text('Iniciar sesi\u00f3n')";
}
