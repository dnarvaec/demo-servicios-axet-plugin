package serenityrest.screenplay.tasks.ecopetrol;

import com.microsoft.playwright.Page;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;
import serenityrest.screenplay.pages.LoginPage;
import serenityrest.utils.AppUrls;

/**
 * Task — Realiza el login en la app Ecopetrol usando Playwright.
 *
 * Flujo:
 *   1. Navegar a /login
 *   2. Ingresar usuario y contraseña
 *   3. Clic en "Iniciar sesión"
 *   4. Esperar redirección a pantalla de selección de rol
 *
 * Uso: actor.attemptsTo(LoginEcopetrol.conCredenciales("jtorres", "abc123"))
 */
public class LoginEcopetrol implements Task {

    private final String usuario;
    private final String contrasena;

    private LoginEcopetrol(String usuario, String contrasena) {
        this.usuario    = usuario;
        this.contrasena = contrasena;
    }

    public static LoginEcopetrol conCredenciales(String usuario, String contrasena) {
        return new LoginEcopetrol(usuario, contrasena);
    }

    @Override
    @Step("{0} inicia sesi\u00f3n en Ecopetrol como #usuario")
    public <T extends Actor> void performAs(T actor) {
        Page page = BrowseTheWebWithPlaywright.as(actor).getCurrentPage();

        // 1. Navegar a la página de login
        page.navigate(AppUrls.ECOPETROL_LOGIN);

        // 2. Esperar a que el campo de usuario esté disponible
        page.waitForSelector(LoginPage.CAMPO_USUARIO);

        // 3. Ingresar credenciales
        page.fill(LoginPage.CAMPO_USUARIO,  usuario);
        page.fill(LoginPage.CAMPO_PASSWORD, contrasena);

        // 4. Clic en "Iniciar sesión"
        page.click(LoginPage.BOTON_LOGIN);

        // 5. Esperar a que aparezca la pantalla de selección de rol
        page.waitForSelector("text=Bienvenidos");
    }
}
