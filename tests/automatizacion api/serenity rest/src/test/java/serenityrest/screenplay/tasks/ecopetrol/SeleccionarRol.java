package serenityrest.screenplay.tasks.ecopetrol;

import com.microsoft.playwright.Page;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;
import serenityrest.screenplay.pages.RolSelectionPage;

/**
 * Task — Selecciona un rol de usuario en la pantalla de bienvenida de Ecopetrol.
 *
 * Playwright usa el selector "text=..." para localizar elementos por texto visible,
 * lo que resuelve el problema de los cards Angular que no responden a clics simples
 * de Puppeteer (event binding de Angular requiere auto-wait nativo de Playwright).
 *
 * Uso: actor.attemptsTo(SeleccionarRol.lider())
 *      actor.attemptsTo(SeleccionarRol.empleado())
 */
public class SeleccionarRol implements Task {

    private final String selectorCard;
    private final String nombreRol;

    private SeleccionarRol(String selectorCard, String nombreRol) {
        this.selectorCard = selectorCard;
        this.nombreRol    = nombreRol;
    }

    /** Selecciona el rol "Líder" */
    public static SeleccionarRol lider() {
        return new SeleccionarRol(RolSelectionPage.CARD_LIDER, "L\u00edder");
    }

    /** Selecciona el rol "Empleado" */
    public static SeleccionarRol empleado() {
        return new SeleccionarRol(RolSelectionPage.CARD_EMPLEADO, "Empleado");
    }

    /** Selecciona el rol "People" */
    public static SeleccionarRol people() {
        return new SeleccionarRol(RolSelectionPage.CARD_PEOPLE, "People");
    }

    @Override
    @Step("{0} selecciona el rol #nombreRol en la pantalla de bienvenida")
    public <T extends Actor> void performAs(T actor) {
        Page page = BrowseTheWebWithPlaywright.as(actor).getCurrentPage();

        // Esperar a que los cards estén visibles
        page.waitForSelector(RolSelectionPage.TITULO_BIENVENIDOS);
        page.waitForSelector(selectorCard);

        // Hacer clic en el card del rol seleccionado
        // Playwright auto-wait garantiza que el evento Angular esté listo
        page.click(selectorCard);

        // Esperar a que la navegación al dashboard se complete
        page.waitForLoadState();
    }
}
